package com.GameHub_Store.usuarios_mscv.repositories;

import com.GameHub_Store.usuarios_mscv.models.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuarios, Long> {

    // findBy + Username -> Genera automáticamente: SELECT * FROM usuarios WHERE username = ?
    // Es indispensable para el proceso de autenticación y generación de JWT.
    Optional<Usuarios> findByUsername(String username);

    // existsBy + Username -> Devuelve un booleano (true/false).
    // Lo utiliza el DataLoader y la capa de registro para evitar la duplicación de cuentas.
    boolean existsByUsername(String username);
}
