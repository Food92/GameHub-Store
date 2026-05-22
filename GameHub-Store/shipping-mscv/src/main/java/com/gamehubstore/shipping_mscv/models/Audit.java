package com.gamehubstore.shipping_mscv.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;

    private Long shippingId;
    private String estadoAnterior;
    private String estadoNuevo;
    private LocalDateTime fechaCambio;

}
