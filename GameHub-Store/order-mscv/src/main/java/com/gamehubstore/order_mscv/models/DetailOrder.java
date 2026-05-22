package com.gamehubstore.order_mscv.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "order-details")

public class DetailOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;


    @NotNull(message = "El ordenId es obligatorio")
    @Column(nullable = false)
    private Long orderId;


    @NotNull(message = "El productoId es obligatorio")
    @Column(nullable = false)
    private Long productId;


    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio unitario debe ser mayor a 0")
    @Column(nullable = false)
    private Double precioUnitario;

}
