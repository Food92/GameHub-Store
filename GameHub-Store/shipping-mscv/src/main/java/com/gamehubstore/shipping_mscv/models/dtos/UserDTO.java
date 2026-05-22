package com.gamehubstore.shipping_mscv.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String nombreCompleto;
    private String apellidoCompleto;
    private String correo;
    private String telefono;
    private Boolean estado;
    private String direccion;
}