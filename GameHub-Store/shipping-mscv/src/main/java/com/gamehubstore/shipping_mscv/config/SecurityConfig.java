package com.gamehubstore.shipping_mscv.config;

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

@Configuration
public class SecurityConfig {

    // Clave simétrica compartida para validar de manera autónoma las firmas de los tokens
    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName("roles");
        authorities.setAuthorityPrefix("");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        return converter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationConverter converter) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // API REST Stateless
                .authorizeHttpRequests(auth -> auth
                        // 1. Permitir acceso público a la documentación técnica
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // 2. CONSULTAR TRACKING / ENVÍOS (GET): Permitido para todos (El gamer ve su envío, el staff controla)
                        .requestMatchers(HttpMethod.GET, "/api/v1/shippings/**")
                        .hasAnyRole("ADMIN", "VENDEDOR", "CLIENTE")

                        // 3. CREAR ENVÍO (POST): El cliente o el vendedor pueden registrar el destino del paquete
                        .requestMatchers(HttpMethod.POST, "/api/v1/shippings/**")
                        .hasAnyRole("ADMIN", "VENDEDOR", "CLIENTE")

                        // 4. MODIFICAR RUTAS Y ESTADOS (PUT / PATCH): Operación crucial y exclusiva de VENDEDOR y ADMIN
                        .requestMatchers("/api/v1/shippings/**")
                        .hasAnyRole("ADMIN", "VENDEDOR")

                        .anyRequest().authenticated())

                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(converter)));

        return http.build();
    }
}
