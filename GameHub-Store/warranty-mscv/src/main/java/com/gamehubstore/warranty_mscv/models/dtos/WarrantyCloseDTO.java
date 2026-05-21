package com.gamehubstore.warranty_mscv.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class WarrantyCloseDTO {
    private String resolucion;
    private String estado;
}
