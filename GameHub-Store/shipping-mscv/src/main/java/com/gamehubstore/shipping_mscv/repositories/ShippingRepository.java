package com.gamehubstore.shipping_mscv.repositories;

import com.gamehubstore.shipping_mscv.models.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {
    List<Shipping> findByUserId(Long userId);
    List<Shipping> findByOrderId(Long orderId);
    List<Shipping> findByEstado(String estado);
}
