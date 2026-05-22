package com.gamehubstore.shipping_mscv.repositories;

import com.gamehubstore.shipping_mscv.models.Audit;
import com.gamehubstore.shipping_mscv.models.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditRepository  extends JpaRepository<Audit, Long> {
    List<Audit> findByshippingId(Long shippingId);
}
