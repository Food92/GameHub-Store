package com.gamehubstore.shipping_mscv.clients;

import com.gamehubstore.shipping_mscv.models.dtos.DireccionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "direccion-service", url = "http://localhost:8003/api/v1/users")
public interface DireccionClient {
    @GetMapping("/{userId}/direcciones")
    List<DireccionDTO> getDireccionesByUserId(@PathVariable Long userId);
}

