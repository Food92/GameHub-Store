package com.gamehubstore.warranty_mscv.controllers;

import com.gamehubstore.warranty_mscv.models.Warranty;
import com.gamehubstore.warranty_mscv.models.dtos.WarrantyCloseDTO;
import com.gamehubstore.warranty_mscv.services.WarrantyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Garantías", description = "Procesamiento de reclamos, soporte técnico y devoluciones")
@SecurityRequirement(name = "bearer-jwt") // 👈 Candado JWT listo
@RequestMapping("/api/v1/warranties")

public class WarrantyController {
    @Autowired
    private WarrantyService warrantyService;

    @PostMapping
    public ResponseEntity<Warranty> save(@RequestBody Warranty garantia) {
        return ResponseEntity.status(HttpStatus.CREATED).body(warrantyService.save(garantia));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Warranty>> findAllByUserId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(warrantyService.findAllByUserId(usuarioId));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Warranty>> findAllByProductId(@PathVariable Long productoId) {
        return ResponseEntity.ok(warrantyService.findAllByProductId(productoId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Warranty>> findAllByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(warrantyService.findAllByEstado(estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Warranty> findById(@PathVariable("id") Long warrantyId) {
        return ResponseEntity.ok(warrantyService.findById(warrantyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Warranty> update(@PathVariable("id") Long warrantyId, @RequestBody Warranty warranty) {
        return ResponseEntity.ok(warrantyService.update(warrantyId, warranty));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<Warranty> close(@PathVariable("id") Long warrantyId,
                                          @RequestBody WarrantyCloseDTO request) {
        return ResponseEntity.ok(warrantyService.closeWarranty(warrantyId, request));
    }

    @GetMapping
    public ResponseEntity<List<Warranty>> findAll() {
        return ResponseEntity.ok(warrantyService.findAll());
    }
}
