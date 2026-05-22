package com.gamehubstore.shipping_mscv.clients;


import com.gamehubstore.shipping_mscv.models.dtos.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8003/api/v1/users")
public interface UserClient {
    @GetMapping("/{userId}")
    UserDTO getUserById(@PathVariable("userId") Long userId);
}

