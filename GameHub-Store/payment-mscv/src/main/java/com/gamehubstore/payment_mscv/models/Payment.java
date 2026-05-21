package com.gamehubstore.payment_mscv.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NotNull
@Setter
@Getter
@ToString
@NoArgsConstructor
@Table(name = "payments")
@Entity

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @NotNull
    @Column(nullable = false)
    private Long orderId;

    @Positive
    @Column(nullable = false)
    private Double monto;

    @NotBlank
    @Column(nullable = false)
    private String tipoPago;// EJ: TARJETA, TRANSFERENCIA, PAYPAL

    @NotBlank
    @Column(nullable = false)
    private String estadoPago; // EJ: PENDIENTE, APROBADO, ANULADO

    @NotBlank
    @Column(nullable = false, unique = true)
    private String codigoPago;

    @PastOrPresent(message = "La fecha no puede ser futura")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaPago;

    private Audit audit= new Audit();


}
