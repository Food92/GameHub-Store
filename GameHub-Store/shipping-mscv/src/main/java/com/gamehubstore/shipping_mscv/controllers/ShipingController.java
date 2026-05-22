package com.gamehubstore.shipping_mscv.controllers;

import com.gamehubstore.shipping_mscv.models.Audit;
import com.gamehubstore.shipping_mscv.models.Shipping;
import com.gamehubstore.shipping_mscv.models.dtos.CancelShippingDTO;
import com.gamehubstore.shipping_mscv.models.dtos.ShippingDTO;
import com.gamehubstore.shipping_mscv.models.dtos.UpdateShippingDTO;
import com.gamehubstore.shipping_mscv.services.ShippingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Validated
@RestController
@RequestMapping("/api/v1/shipping")
public class ShipingController {

    @Autowired
    private ShippingService shippingService;

    // Crear un nuevo envío
    @PostMapping
    public ResponseEntity<ShippingDTO> crearShipping(@Valid @RequestBody ShippingDTO dto) {
        return ResponseEntity.ok(shippingService.save(dto));
    }

    // Actualizar un envío existente
    @PutMapping("/{id}")
    public ResponseEntity<Shipping> actualizarShipping(@PathVariable Long id,
                                                       @Valid @RequestBody ShippingDTO dto) {
        return ResponseEntity.ok(shippingService.update(dto, id));
    }

    // Cancelar un envío
    @DeleteMapping
    public ResponseEntity<Void> cancelarShipping(@Valid @RequestBody CancelShippingDTO dto) {
        shippingService.cancel(dto);
        return ResponseEntity.noContent().build();
    }

    // Listar todos los envíos
    @GetMapping
    public ResponseEntity<List<Shipping>> listarTodos() {
        return ResponseEntity.ok(shippingService.findAll());
    }

    // Listar envíos por usuario
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Shipping>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(shippingService.findByUserId(userId));
    }

    // Listar envíos por orden
    @GetMapping("/orden/{orderId}")
    public ResponseEntity<List<Shipping>> findByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(shippingService.findByOrderId(orderId));
    }

    // Listar envíos por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Shipping>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(shippingService.findByEstado(estado));
    }

    // ✅ Nuevo: consultar auditoría por envío
    @GetMapping("/audit/{shippingId}")
    public ResponseEntity<List<Audit>> obtenerAuditoria(@PathVariable Long shippingId) {
        return ResponseEntity.ok(shippingService.findAuditByShippingId(shippingId));
    }

}
