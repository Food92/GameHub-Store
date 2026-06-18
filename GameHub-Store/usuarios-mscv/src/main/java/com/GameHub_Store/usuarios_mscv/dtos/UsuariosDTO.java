package com.GameHub_Store.usuarios_mscv.dtos;

import lombok.*;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class UsuariosDTO {
    private Long usuarioId;
    private String username;
    private Set<String> roles;
}
