package com.gamehubstore.order_mscv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

// Seguridad del microservicio de órdenes (order_mscv).
//
// Este servicio actúa como "Resource Server": no hace login ni emite tokens (eso es de usuarios_mscv),
// solo RECIBE peticiones que traen un JWT en la cabecera "Authorization: Bearer <token>", verifica que
// el token sea válido y decide, según el rol, si deja pasar o no.
//
// Es la segunda barrera de seguridad: aunque alguien evite el gateway y llame directo al puerto de órdenes,
// igual tiene que presentar un token válido y con el rol correcto.
@Configuration
public class SecurityConfig {

    // Se lee del application.properties. Es la MISMA clave con la que usuarios_mscv firmó el token;
    // por eso aquí podemos verificar que la firma es auténtica.
    @Value("${jwt.secret}")
    private String secret;

    // Construye la clave HMAC-SHA256 a partir del texto del secreto.
    // HS256 es un algoritmo "simétrico": la misma clave sirve para firmar y para verificar.
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        // El decoder verifica DOS cosas en cada petición: que la firma del token sea correcta
        // (no fue alterado) y que no esté vencido (claim exp). Si algo falla, responde 401.
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }

    // El token guarda los roles en un claim llamado "roles" (ej: ["ROLE_VENDEDOR"]).
    // Spring necesita convertir esos textos en "authorities" para poder evaluarlos con hasRole(...).
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName("roles"); // de qué claim del token leer los roles
        authorities.setAuthorityPrefix("");           // sin prefijo extra: ya guardamos "ROLE_..." completo
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        return converter;
    }

    // Aquí se define la cadena de filtros de seguridad: las reglas de quién puede entrar a qué.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationConverter converter) throws Exception {
        http
                // CSRF protege formularios web con sesión. Como esta es una API sin estado (token en cada
                // request) no aplica, así que se desactiva para no bloquear los POST/PUT/PATCH/DELETE.
                .csrf(csrf -> csrf.disable())
                // Las reglas se evalúan EN ORDEN: la primera que coincide con la petición manda.
                .authorizeHttpRequests(auth -> auth
                        // Documentación y entorno Swagger UI: abiertas (sin token) para poder mostrarlas en clase.
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // LEER ÓRDENES (GET): lo permite cualquier usuario autenticado en la tienda gamer (Clientes o Staff).
                        // Va PRIMERO que las reglas de abajo para que las lecturas no caigan en restricciones críticas.
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/**")
                        .hasAnyRole("ADMIN", "VENDEDOR", "CLIENTE")

                        // CAMBIAR ESTADO DE LA ORDEN (PATCH): Endpoint exclusivo del flujo de trabajo del Vendedor
                        // (para pasar los pedidos pagados a "EN_DESPACHO" o "ENTREGADO") y del Administrador.
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/orders/*/estado")
                        .hasAnyRole("ADMIN", "VENDEDOR")

                        // ESCRIBIR / CREAR ÓRDENES (POST/PUT/DELETE): El cliente puede generar compras (POST),
                        // y el equipo administrativo/vendedores puede gestionar o actualizar las órdenes.
                        .requestMatchers("/api/v1/orders/**")
                        .hasAnyRole("ADMIN", "VENDEDOR", "CLIENTE")

                        // Cualquier otra ruta no listada: basta con estar autenticado.
                        .anyRequest().authenticated())
                // STATELESS: el servidor NO guarda sesión. Cada petición se autentica sola con su token.
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Activa la validación del JWT usando el decoder y el conversor de roles de arriba.
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(converter)));

        return http.build();
    }
}
