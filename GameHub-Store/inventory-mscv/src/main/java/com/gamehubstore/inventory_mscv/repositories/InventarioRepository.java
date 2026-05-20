package com.gamehubstore.inventory_mscv.repositories;

import com.gamehubstore.inventory_mscv.models.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    List<Inventario> findByIdProducto(Long idProducto);
    List<Inventario> findByUbicacion(String ubicacion);
}

