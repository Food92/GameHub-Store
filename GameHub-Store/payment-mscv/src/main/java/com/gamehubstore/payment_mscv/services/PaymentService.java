package com.gamehubstore.payment_mscv.services;

import com.gamehubstore.payment_mscv.models.Payment;
import com.gamehubstore.payment_mscv.models.dtos.PaymentDTO;

import java.util.List;

public interface PaymentService {
    Payment save(PaymentDTO paymentDTO);
    List<Payment> findByOrder(Long orderId);
    List<Payment> findByEstadoPago(String estadoPago);
    List<Payment> findByTipoPago(String tipoPago);
    Payment findById(Long id);
    Payment updatepayment(String estadoPago, Long paymentId );
    Payment cancelpayment(Long paymentId);
    List<Payment> findAll();
}
