package com.gamehubstore.shipping_mscv.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "shipping")
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long shippingId;


    @NotNull(message = "El ID de la orden es obligatorio")
    private Long orderId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotBlank(message = "La direccion es obligatoria")
    private String direccion;


    @NotBlank(message = "El transportista es obligatorio")
    private String transportista;


    @Column(unique = true)
    private String tracking;


    @NotBlank(message = "El estado es obligatorio")
    private String estado; // CREADO, EN_TRANSITO, ENTREGADO, CANCELADO


    private LocalDate fechaEnvio;

}
