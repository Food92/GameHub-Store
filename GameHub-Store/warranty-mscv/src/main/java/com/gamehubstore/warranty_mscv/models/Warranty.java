package com.gamehubstore.warranty_mscv.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Entity
@Table(name = "warranties")
public class Warranty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int warrantyId;

    @NotNull(message = "El usuarioId es obligatorio")
    private Long userId;

    @NotNull(message = "El productId es obligatorio")
    private Long productId;


    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

    @NotBlank(message = "El estado es obligatorio")
    private  String estado; // ej: Abierta, En_revision, Cerrada

    @PastOrPresent(message = "La fecha de solicitud no puede ser futura")
    private LocalDateTime fechaSolicitud;


    private  String resolucion;

    @Embedded
    private Audit audit=new Audit();

}
