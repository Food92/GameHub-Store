package com.gamehubstore.payment_mscv.services;

import com.gamehubstore.payment_mscv.client.OrderClient;
import com.gamehubstore.payment_mscv.exceptions.PaymentException;
import com.gamehubstore.payment_mscv.models.Payment;
import com.gamehubstore.payment_mscv.models.dtos.OrderDTO;
import com.gamehubstore.payment_mscv.models.dtos.PaymentDTO;
import com.gamehubstore.payment_mscv.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderClient orderClient;


    @Override
    public Payment save(PaymentDTO paymentDTO) {
        // Validar orden
        OrderDTO order = orderClient.getOrderById(paymentDTO.getOrderId());
        if (order == null) {
            throw new PaymentException("Order no existe");
        }

        // Validar monto
        if (!order.getTotal().equals(paymentDTO.getMonto())) {
            throw new PaymentException("El monto no coincide con el total de la orden");
        }

        // Evitar duplicados
        if (paymentRepository.findByOrderId(paymentDTO.getOrderId())
                .stream().anyMatch(p -> "APROBADO".equals(p.getEstadoPago()))) {
            throw new RuntimeException("Ya existe un pago aprobado para esta orden");
        }

        // Crear pago
        Payment payment = new Payment();
        payment.setOrderId(paymentDTO.getOrderId());   // 👈 aquí va orderId, no paymentId
        payment.setMonto(paymentDTO.getMonto());
        payment.setTipoPago(paymentDTO.getTipoPago());
        payment.setEstadoPago(paymentDTO.getEstadoPago());
        payment.setCodigoPago(UUID.randomUUID().toString());
        payment.setFechaPago(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        return paymentRepository.save(payment);
    }




    @Override
    public List<Payment> findByOrder(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }



    @Override
    public List<Payment> findByEstadoPago(String estadoPago) {
        return paymentRepository.findByEstadoPago(estadoPago);
    }



    @Override
    public List<Payment> findByTipoPago(String tipoPago) {
        return paymentRepository.findByTipoPago(tipoPago);
    }



    @Override
    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElseThrow(
                ()-> new  PaymentException("Payment no encontrado"));
    }



    @Override
    public Payment updatepayment(String estadoPago, Long paymentId) {
        Payment existing = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException("Payment no encontrado"));

        existing.setEstadoPago(estadoPago);
        return paymentRepository.save(existing);
    }



    @Override
    public Payment cancelpayment(Long paymentId) {
        Payment existing = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException("Payment no encontrado"));

        existing.setEstadoPago("CANCELADO");
        return paymentRepository.save(existing);
    }



    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

}
