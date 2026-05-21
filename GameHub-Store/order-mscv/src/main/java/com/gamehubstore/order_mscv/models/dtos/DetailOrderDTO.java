package com.gamehubstore.order_mscv.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

public class DetailOrderDTO {
    private   Long productId;
    private   Integer cantidad;
    private   Double precioUnitario;
}
