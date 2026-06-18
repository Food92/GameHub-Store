package com.gamehubstore.payment_mscv.controllers;

import com.gamehubstore.payment_mscv.models.Payment;
import com.gamehubstore.payment_mscv.models.dtos.PaymentDTO;
import com.gamehubstore.payment_mscv.services.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pagos", description = "Procesamiento financiero de órdenes de GameHub Store")
@SecurityRequirement(name = "bearer-jwt")
@RestController
@RequestMapping("/api/v1/payments")

public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    // Crear un pago
    @PostMapping
    public ResponseEntity<Payment> sace(@Valid @RequestBody PaymentDTO dto) {
        return ResponseEntity.ok(paymentService.save(dto));
    }


    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.findAll());
    }


    // Buscar pagos por orden
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payment>> getPaymentsByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.findByOrder(orderId));
    }

    // Buscar pagos por estado
    @GetMapping("/estado/{estadoPago}")
    public ResponseEntity<List<Payment>> getPaymentsByEstado(@PathVariable String estadoPago) {
        return ResponseEntity.ok(paymentService.findByEstadoPago(estadoPago));
    }

    // Buscar pagos por tipo de pago
    @GetMapping("/tipo/{tipoPago}")
    public ResponseEntity<List<Payment>> getPaymentsByTipo(@PathVariable String tipoPago) {
        return ResponseEntity.ok(paymentService.findByTipoPago(tipoPago));
    }

    // Buscar pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    // Actualizar estado de un pago
    @PutMapping("/{id}/estado")
    public ResponseEntity<Payment> updatePaymentEstado(@PathVariable Long id,
                                                       @RequestParam String estadoPago) {
        return ResponseEntity.ok(paymentService.updatepayment(estadoPago, id));
    }

    // Cancelar un pago
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Payment> cancelPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.cancelpayment(id));
    }
}
