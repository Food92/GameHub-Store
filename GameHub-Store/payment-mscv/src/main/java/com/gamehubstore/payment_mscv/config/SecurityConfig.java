package com.gamehubstore.payment_mscv.config;

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

    // Clave simétrica compartida para decodificar los JWT de la plataforma de manera distribuida
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
                        // 1. Acceso público a la documentación técnica
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // 2. CONSULTAR TRANSACCIONES (GET): El cliente ve sus pagos, el Vendedor y Admin validan transacciones corporativas
                        .requestMatchers(HttpMethod.GET, "/api/v1/payments/**")
                        .hasAnyRole("ADMIN", "VENDEDOR", "CLIENTE")

                        // 3. PROCESAR TRANSACCIÓN (POST): Permitido para Clientes (hacer el pago) y personal de Staff
                        .requestMatchers(HttpMethod.POST, "/api/v1/payments").hasAnyRole("ADMIN", "VENDEDOR", "CLIENTE")

                        // 4. REEMBOLSOS Y OPERACIONES CRÍTICAS (POST/PUT especiales como /reembolsar): Restringido exclusivamente al Vendedor y Admin
                        .requestMatchers("/api/v1/payments/**").hasAnyRole("ADMIN", "VENDEDOR")

                        .anyRequest().authenticated())

                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(converter)));

        return http.build();
    }
}
