package com.GameHub_Store.usuarios_mscv.controllers;

import com.GameHub_Store.usuarios_mscv.dtos.AuthenRequest; // 👈 Usamos tu DTO real de respuesta
import com.GameHub_Store.usuarios_mscv.dtos.LoginRequest;
import com.GameHub_Store.usuarios_mscv.dtos.RegisterRequest;
import com.GameHub_Store.usuarios_mscv.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Endpoints públicos para el registro y login de usuarios en GameHub Store. Emite el JWT.")
public class AuthController {

    private final AuthService authService;

    // Inyección por constructor: Desacoplamiento limpio y testeable
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar cuenta gamer", description = "Registra un usuario nuevo en la plataforma y le otorga acceso inmediato. Roles permitidos: ROLE_ADMIN, ROLE_VENDEDOR, ROLE_CLIENTE.")
    public ResponseEntity<AuthenRequest> register(@Valid @RequestBody RegisterRequest request) { // 👈 Tipado con AuthenRequest
        return ResponseEntity.status(HttpStatus.CREATED).body(this.authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida las credenciales contra los hashes de la base de datos y retorna el token JWT firmado.")
    public ResponseEntity<AuthenRequest> login(@Valid @RequestBody LoginRequest request) { // 👈 Tipado con AuthenRequest
        return ResponseEntity.ok(this.authService.login(request));
    }
}