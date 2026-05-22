package com.gamehubstore.shipping_mscv.models.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
@Setter
@Getter
@ToString
@NoArgsConstructor
public class UpdateShippingDTO {
    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    private String tracking; // opcional, puede ser nul
}
