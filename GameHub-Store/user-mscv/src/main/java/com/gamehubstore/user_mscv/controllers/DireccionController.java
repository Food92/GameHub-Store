package com.gamehubstore.user_mscv.controllers;

import com.gamehubstore.user_mscv.models.dtos.DireccionDTO;
import com.gamehubstore.user_mscv.services.DireccionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/direcciones")
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    // Crear dirección para un usuario
    @PostMapping
    public ResponseEntity<DireccionDTO> save (
            @PathVariable Long userId,
            @RequestBody @Valid DireccionDTO direccionDTO) {
        direccionDTO.setUserId(userId); // asignar el userId desde la URL
        DireccionDTO saved = direccionService.save(direccionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Listar direcciones de un usuario
    @GetMapping
    public ResponseEntity<List<DireccionDTO>> listarDirecciones(@PathVariable Long userId) {
        List<DireccionDTO> direcciones = direccionService.findByUserId(userId);
        return ResponseEntity.ok(direcciones);
    }

    // Eliminar dirección por ID
    @DeleteMapping("/{direccionId}")
    public ResponseEntity<Void> eliminarDireccion(@PathVariable Long direccionId) {
        direccionService.delete(direccionId);
        return ResponseEntity.noContent().build();
    }
}
