package com.GameHub_Store.usuarios_mscv.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Setter
@Getter
@NoArgsConstructor

public class Rol {
    @Id
    @Column(name = "rol_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rolId;

    @Column(unique = true, nullable = false)
    private String rolName;

    public Rol(String rolName) {
        this.rolName = rolName;
    }
}
