package com.gamehubstore.shipping_mscv.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private Long userId;
    private String estado;   // CREADA, PAGADA, CANCELADA, EN_DESPACHO
    private Double total;
}
