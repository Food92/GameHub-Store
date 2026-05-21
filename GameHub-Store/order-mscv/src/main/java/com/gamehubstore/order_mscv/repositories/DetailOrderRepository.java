package com.gamehubstore.order_mscv.repositories;

import com.gamehubstore.order_mscv.models.DetailOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailOrderRepository extends CrudRepository<DetailOrder, Long> {
    List<DetailOrder> findByOrderId(Long orderId);
}
