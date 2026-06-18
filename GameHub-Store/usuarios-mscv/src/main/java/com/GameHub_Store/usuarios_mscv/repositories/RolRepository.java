package com.GameHub_Store.usuarios_mscv.repositories;

import com.GameHub_Store.usuarios_mscv.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    // Busca un rol por su atributo real en tu modelo gamer: 'rolName' (ej: "ROLE_ADMIN").
    // Es el método clave que invoca el .findByNombreCategory() / findByRolName() en tu DataLoader.
    Optional<Rol> findByRolName(String rolName);
}