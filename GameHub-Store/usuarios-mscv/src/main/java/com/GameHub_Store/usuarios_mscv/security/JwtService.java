package com.GameHub_Store.usuarios_mscv.security;

import com.GameHub_Store.usuarios_mscv.models.Rol;
import com.GameHub_Store.usuarios_mscv.models.Usuarios;
import org.springframework.beans.factory.annotation.Value; // 👈 CORRECCIÓN 1: Importar el @Value de Spring, NO el de Lombok
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final long expirationMinutes;

    // 👈 CORRECCIÓN 2: Inyectar la propiedad directamente en el constructor para cumplir con las buenas prácticas de Spring
    public JwtService(
            JwtEncoder jwtEncoder,
            @Value("${jwt.expiration-minutes:60}") long expirationMinutes) {
        this.jwtEncoder = jwtEncoder;
        this.expirationMinutes = expirationMinutes;
    }

    public String generarToken(Usuarios usuarios) {
        Instant ahora = Instant.now();

        // Mapeamos los roles reales de tu tienda gamer usando el nombre del atributo corregido: 'getRolName'
        List<String> roles = usuarios.getRoles().stream()
                .map(Rol::getRolName)
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("usuarios-mscv") // Identificador único de este microservicio emisor en GameHub Store
                .issuedAt(ahora)
                .expiresAt(ahora.plus(expirationMinutes, ChronoUnit.MINUTES))
                .subject(usuarios.getUsername()) // El dueño del token (username)
                .claim("roles", roles)          // Adjuntamos los roles en los Claims privados del JWT
                .build();

        // Firma digital con el algoritmo HMAC-SHA256 (clave simétrica compartida)
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
