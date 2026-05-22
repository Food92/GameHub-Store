package com.gamehubstore.shipping_mscv.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ShippingDTO {
    // Para recibir y validar la creación/actualización de un despacho desde el Controller

    private Long shippingId;

    @NotNull(message = "El ID de la orden es obligatorio")
    private Long orderId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    private String estado;

    private String direccion;

    @NotBlank(message = "El transportista es obligatorio")
    private String transportista;

    private String tracking; // generado automáticamente
}
