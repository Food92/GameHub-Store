package com.gamehubstore.payment_mscv.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor

public class PaymentDTO {

    @NotNull(message = "El ordenId es obligatorio")
    private Long orderId;

    @Positive(message = "El monto debe ser mayor a 0")
    private Double monto;

    @NotBlank(message = "El método de pago es obligatorio")
    private String tipoPago; // TARJETA, TRANSFERENCIA, PAYPAL

    @NotBlank(message = "El estado es obligatorio")
    private String estadoPago; // PENDIENTE, APROBADO, ANULADO

    // El código de transacción lo genera el sistema, no se envía desde el cliente
    private String codigoPago;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDateTime fecha;
}
