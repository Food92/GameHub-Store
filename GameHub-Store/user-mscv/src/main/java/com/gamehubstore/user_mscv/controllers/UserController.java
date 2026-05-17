package com.gamehubstore.user_mscv.controllers;

import com.gamehubstore.user_mscv.models.User;
import com.gamehubstore.user_mscv.models.dtos.UserDTO;
import com.gamehubstore.user_mscv.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")

public class UserController {
    @Autowired
    private UserService userService;

    // Buscar usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    // Crear usuario
    @PostMapping
    public ResponseEntity<UserDTO> crearUsuario(@RequestBody @Valid UserDTO userDTO) {
        UserDTO savedUser = userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody @Valid User user) {
        User updatedUser = userService.update(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById (@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Desactivar usuario
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable Long id) {
        userService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<User>> listarUsuarios() {
        return ResponseEntity.ok(userService.findAll());
    }

    // Listar usuarios activos
    @GetMapping("/activos")
    public ResponseEntity<List<User>> listarUsuariosActivos() {
        return ResponseEntity.ok(userService.findByEstadoTrue());
    }

    // Listar usuarios inactivos
    @GetMapping("/inactivos")
    public ResponseEntity<List<User>> listarUsuariosInactivos() {
        return ResponseEntity.ok(userService.findByEstadoFalse());
    }

}
