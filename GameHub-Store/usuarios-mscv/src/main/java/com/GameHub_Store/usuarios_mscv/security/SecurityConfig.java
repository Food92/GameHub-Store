package com.GameHub_Store.usuarios_mscv.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value; // 👈 CORRECCIÓN 1: Import de Spring, no el de Lombok
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // 👈 MEJORA: Habilita @PreAuthorize de forma moderna
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm; // 👈 CORRECCIÓN 2: Import correcto del algoritmo MAC para JWT
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Reemplaza la anotación en comentario para activar de manera real el uso de @PreAuthorize en Controllers
public class SecurityConfig {

    // Clave secreta COMPARTIDA por todos los servicios de la tienda gamer.
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey claveHmac() {
        // HS256 necesita una clave de al menos 256 bits (32 bytes).
        return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    // Componente encargado de FIRMAR los tokens JWT (Capa emisora)
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(claveHmac()));
    }

    // Componente encargado de VALIDAR los tokens criptográficamente (Capa receptora)
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(claveHmac()).macAlgorithm(MacAlgorithm.HS256).build();
    }

    // Cifra las contraseñas utilizando hashing adaptativo (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Extrae los roles firmados en el token y los inyecta en el contexto de Spring Security
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName("roles");
        authorities.setAuthorityPrefix(""); // Prefijo vacío porque la BD ya guarda "ROLE_ADMIN", "ROLE_CLIENTE"
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        return converter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationConverter converter) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitado porque no usamos cookies ni estado en sesión (Stateless)
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas: Autenticación, registro y endpoints de documentación interactiva
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/docs/**", "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll()
                        // Cualquier otra petición a la tienda requiere pasar un JWT válido
                        .anyRequest().authenticated())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configura este microservicio como Resource Server para resguardar sus propios endpoints protegidos
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(converter)))
                .headers(h -> h.frameOptions(f -> f.disable())); // Permitir la renderización en frames para la consola H2

        return http.build();
    }
}
