package com.gamehubstore.payment_mscv.client;

import com.gamehubstore.payment_mscv.models.dtos.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service", url = "http://localhost:8087/api/v1/orders")
public interface OrderClient {

    @GetMapping("/{orderId}")
    OrderDTO getOrderById(@PathVariable("orderId") Long orderId);

    @GetMapping("/user/{userId}")
    List<OrderDTO> getUserOrders(@PathVariable("userId") Long userId);
}


