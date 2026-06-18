package com.GameHub_Store.usuarios_mscv.controllers;

import com.GameHub_Store.usuarios_mscv.dtos.UsuariosDTO;
import com.GameHub_Store.usuarios_mscv.models.Rol;
import com.GameHub_Store.usuarios_mscv.repositories.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Gestión administrativa de cuentas (Requiere privilegios)")
@SecurityRequirement(name = "bearer-jwt")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    // Inyección por constructor para asegurar un desacoplamiento limpio
    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Uso exclusivo para ROLE_ADMIN. Retorna la vista segura DTO.")
    // @PreAuthorize evalúa el token antes de dar paso al método. Intercepta y exige 'ROLE_ADMIN'.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuariosDTO>> findAll() {
        List<UsuariosDTO> usuarios = this.usuarioRepository.findAll().stream()
                .map(u -> new UsuariosDTO(
                        u.getUsuarioId(),
                        u.getUsername(),
                        u.getRoles().stream().map(Rol::getRolName).collect(Collectors.toSet()))) // 👈 Cambiado a getRolName
                .toList();

        return ResponseEntity.ok(usuarios);
    }
}