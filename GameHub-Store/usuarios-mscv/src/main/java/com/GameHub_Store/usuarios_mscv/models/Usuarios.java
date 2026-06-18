package com.GameHub_Store.usuarios_mscv.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Setter
@Getter
@NoArgsConstructor
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental obligatorio para la persistencia
    @Column(name = "usuario_id")
    private Long usuarioId;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Column(unique = true, nullable = false)
    private String username;

    // Se guarda CIFRADA con BCrypt, nunca en texto plano.
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Column(nullable = false)
    private String password;

    // Relación muchos-a-muchos: un usuario tiene varios roles y un rol lo comparten varios usuarios.
    // Se crea una tabla intermedia 'usuario_roles' con las dos llaves foraneas.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();
}
