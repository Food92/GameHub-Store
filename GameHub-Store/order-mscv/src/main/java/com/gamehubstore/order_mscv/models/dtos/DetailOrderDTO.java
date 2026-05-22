package com.gamehubstore.order_mscv.models.dtos;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Embeddable
public class DetailOrderDTO {
    @NotNull(message = "El producto es obligatorio")
    private Long productId;

    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @PositiveOrZero(message = "El precio unitario debe ser mayor o igual a 0")
    private Double precioUnitario;
}
