package com.gamehubstore.payment_mscv.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderDTO {
    @NotNull
    private Long userId;

    @PositiveOrZero
    private Double total;


    @NotBlank(message = "El estado no puede ser vacío")
    private String estado;

}
