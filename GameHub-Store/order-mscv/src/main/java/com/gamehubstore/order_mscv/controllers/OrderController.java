package com.gamehubstore.order_mscv.controllers;

import com.gamehubstore.order_mscv.models.Order;
import com.gamehubstore.order_mscv.models.dtos.OrderDTO;
import com.gamehubstore.order_mscv.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 🟢 Crear orden
    @PostMapping
    public ResponseEntity<OrderDTO> create(@Valid @RequestBody OrderDTO dto) {
        OrderDTO saved = orderService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    // 🟡 Actualizar orden
    @PutMapping("/{orderId}")
    public ResponseEntity<Order> update(@PathVariable Long orderId, @Valid @RequestBody OrderDTO dto) {
        Order updated = orderService.update(dto, orderId);
        return ResponseEntity.ok(updated);
    }

    // 🔵 Buscar por ID
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> findById(@PathVariable Long orderId) {
        OrderDTO dto = orderService.findByIdDTO(orderId);
        return ResponseEntity.ok(dto);
    }


    // 🟣 Listar todas
    @GetMapping
    public ResponseEntity<List<Order>> findAll() {
        List<Order> orders = orderService.findAll();
        return ResponseEntity.ok(orders);
    }

    // 🟠 Listar por usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> findByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.findByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    // 🔴 Listar por estado
    @PutMapping("/{orderId}/estado")
    public ResponseEntity<OrderDTO> updateEstado(@PathVariable Long orderId,
                                                 @RequestParam String estado) {
        OrderDTO actualizado = orderService.updateEstado(orderId, estado);
        return ResponseEntity.ok(actualizado);
    }

    // ⚫ Cancelar orden
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
        Order canceled = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(canceled);
    }


}
