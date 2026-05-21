package com.gamehubstore.order_mscv.repositories;

import com.gamehubstore.order_mscv.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface OrderRepository extends JpaRepository<Order,Long> {

    // Listar órdenes por usuario
    List<Order> findByUserId(Long userId);

    // Listar órdenes por estado
    List<Order> findByEstado(String estado);

}
