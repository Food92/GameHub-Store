package com.gamehubstore.order_mscv.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor

public class OrderDTO {


    private Long orderId;

    @NotNull
    @PositiveOrZero
    @NotNull
    private Long userId;

    @PositiveOrZero
    private Double descuento; // opcional

    @NotBlank(message = "El estado no puede ser vacío")
    private String estado;

    @PositiveOrZero
    private Double subtotal; //

    @PositiveOrZero
    private Double total; //

    @NotNull
    private List<DetailOrderDTO> details;
}