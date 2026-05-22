package com.gamehubstore.order_mscv.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@Table(name = "orders")

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @NotNull(message = "El idUser es obligatorio")
    @Column(nullable = false)
    private Long userId;


    @NotBlank(message = "El estado no puede ser vacío")
    @Column(nullable = false)
    private String estado;// CREADA, PAGADA, CANCELADA, EN_DESPACHO


    @PositiveOrZero(message = "El subtotal debe ser mayor o igual a 0")
    @Column(nullable = false)
    private Double subtotal;


    @PositiveOrZero(message = "El descuento debe ser mayor o igual a 0")
    private Double descuento;


    @Positive(message = "El total debe ser mayor a 0")
    @Column(nullable = false)
    private Double total;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @PastOrPresent
    private LocalDateTime fecha;


    @Transient // si no quieres que JPA lo persista
    private List<DetailOrder> details;

    private Audit audit = new Audit();

}
