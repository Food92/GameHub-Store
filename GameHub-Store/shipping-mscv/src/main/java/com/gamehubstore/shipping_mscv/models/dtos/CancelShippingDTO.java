package com.gamehubstore.shipping_mscv.models.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CancelShippingDTO {
    @NotNull(message = "El ID del despacho es obligatorio")
    private Long shippingId;
}
