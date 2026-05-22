package com.gamehubstore.shipping_mscv.clients;

import com.gamehubstore.shipping_mscv.models.dtos.OrderDTO;
import jakarta.persistence.criteria.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "http://localhost:8087/api/v1/orders")
public interface OrderClient {
    @GetMapping("/{orderId}")
    OrderDTO findById(@PathVariable("orderId") Long orderId);
}
