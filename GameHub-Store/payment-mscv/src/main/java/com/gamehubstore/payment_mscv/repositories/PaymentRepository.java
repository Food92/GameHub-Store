package com.gamehubstore.payment_mscv.repositories;

import com.gamehubstore.payment_mscv.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrderId(Long orderId);
    List<Payment> findByCodigoPago(String codigoPago);
    List<Payment> findByEstadoPago(String estadoPago);
    List<Payment> findByTipoPago(String tipoPago);
}
