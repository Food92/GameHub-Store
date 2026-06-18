package com.GameHub_Store.usuarios_mscv.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

// Respuesta del login/registro: el token JWT y datos basicos del usuario.

public class AuthenRequest {
    private String token;
    private String tokenType;
    private String username;
    private Set<String> roles;

}
