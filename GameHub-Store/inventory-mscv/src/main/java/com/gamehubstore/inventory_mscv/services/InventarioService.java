package com.gamehubstore.inventory_mscv.services;

import com.gamehubstore.inventory_mscv.models.Inventario;

import java.util.List;

public interface InventarioService {
    Inventario findById(Long id);
    Inventario save(Inventario inventario);
    Inventario update(Inventario inventario, Long id);
    Inventario reservarStock(Long id, Long cantidad);
    void deleteById(Long id);
    List<Inventario> findByProducto(Long idProducto);
    List<Inventario> findByUbicacion(String ubicacion);
    List<Inventario> findAll();
    Inventario salidaStock(Long id, Long cantidad); // 👈 nuevo método

}
