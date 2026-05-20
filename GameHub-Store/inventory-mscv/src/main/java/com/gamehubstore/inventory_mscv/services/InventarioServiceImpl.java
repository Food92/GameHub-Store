package com.gamehubstore.inventory_mscv.services;


import com.gamehubstore.inventory_mscv.client.ProductClient;
import com.gamehubstore.inventory_mscv.exceptions.InventarioException;
import com.gamehubstore.inventory_mscv.models.Inventario;
import com.gamehubstore.inventory_mscv.models.MovimientoInventario;
import com.gamehubstore.inventory_mscv.models.dtos.ProductDTO;
import com.gamehubstore.inventory_mscv.repositories.InventarioRepository;
import com.gamehubstore.inventory_mscv.repositories.MovimientoInventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Autowired
    private ProductClient productClient;

    @Override
    public List<Inventario> findAll() {
        return inventarioRepository.findAll();
    }



    @Override
    public Inventario findById(Long id) {
        return null;
    }

    @Override
    public Inventario save(Inventario inventario) {
        ProductDTO productDTO = productClient.findById(inventario.getIdProducto());

        if (productDTO == null || productDTO.getEstado() == Inventario.EstadoProducto.INACTIVO) {
            throw new InventarioException("No existe el producto o está inactivo: " + inventario.getIdProducto());
        }

        if (inventario.getStockDisponible() < 0) {
            throw new InventarioException("El stock inicial no puede ser negativo");
        }

        Inventario saved = inventarioRepository.save(inventario);

        MovimientoInventario mov = new MovimientoInventario();
        mov.setIdProducto(inventario.getIdProducto());
        mov.setTipo("ENTRADA");
        mov.setCantidad(inventario.getStockDisponible());
        mov.setFecha(LocalDateTime.now());
        movimientoInventarioRepository.save(mov);

        return saved;
    }




    @Override
    public Inventario update(Inventario inventario, Long id) {
        Inventario existente = inventarioRepository.findById(id).orElseThrow(
                () -> new InventarioException("Inventario con ID: " + id + " no existe"));

        // Validar que stock no quede negativo
        if (inventario.getStockDisponible() < 0 || inventario.getStockReservado() < 0) {
            throw new InventarioException("Stock no puede quedar negativo");
        }

        // Actualizamos los campos
        existente.setUbicacion(inventario.getUbicacion());
        existente.setStockDisponible(inventario.getStockDisponible());
        existente.setStockReservado(inventario.getStockReservado());

        Inventario actualizado = inventarioRepository.save(existente);

        // Registrar movimiento de ajuste
        MovimientoInventario mov = new MovimientoInventario();
        mov.setIdProducto(actualizado.getIdProducto());
        mov.setTipo("AJUSTE");
        mov.setCantidad(inventario.getStockDisponible());
        mov.setFecha(LocalDateTime.now());
        movimientoInventarioRepository.save(mov);

        return actualizado;
    }




    @Override
    public Inventario reservarStock(Long id, Long cantidad) {
        Inventario inventario = inventarioRepository.findById(id).orElseThrow(
                () -> new InventarioException("Inventario con ID: " + id + " no existe"));

        if (cantidad <= 0) {
            throw new InventarioException("La cantidad a reservar debe ser mayor a cero");
        }

        if (inventario.getStockDisponible() < cantidad) {
            throw new InventarioException("Stock insuficiente para reservar");
        }

        inventario.setStockDisponible(inventario.getStockDisponible() - cantidad);
        inventario.setStockReservado(inventario.getStockReservado() + cantidad);

        // Registrar movimiento
        MovimientoInventario mov = new MovimientoInventario();
        mov.setIdProducto(inventario.getIdProducto());
        mov.setTipo("RESERVA");
        mov.setCantidad(cantidad);
        mov.setFecha(LocalDateTime.now());
        movimientoInventarioRepository.save(mov);

        return inventarioRepository.save(inventario);
    }



    @Override
    public Inventario salidaStock(Long id, Long cantidad) {
        Inventario inventario = inventarioRepository.findById(id).orElseThrow(
                () -> new InventarioException("Inventario con ID: " + id + " no existe"));

        if (cantidad <= 0) {
            throw new InventarioException("La cantidad de salida debe ser mayor a cero");
        }

        if (inventario.getStockDisponible() < cantidad) {
            throw new InventarioException("Stock insuficiente, no puede quedar negativo");
        }

        // Actualizar stock
        inventario.setStockDisponible(inventario.getStockDisponible() - cantidad);

        // Registrar movimiento
        MovimientoInventario mov = new MovimientoInventario();
        mov.setIdProducto(inventario.getIdProducto());
        mov.setTipo("SALIDA");
        mov.setCantidad(cantidad);
        mov.setFecha(LocalDateTime.now());
        movimientoInventarioRepository.save(mov);

        return inventarioRepository.save(inventario);
    }



    @Override
    public void deleteById(Long id) {
        if (!inventarioRepository.existsById(id)) {
            throw new InventarioException("Inventario con id " + id + " no existe");
        }
        inventarioRepository.deleteById(id);
    }





    @Override
    public List<Inventario> findByProducto(Long idProducto) {
        return inventarioRepository.findByIdProducto(idProducto);
    }



    
    @Override
    public List<Inventario> findByUbicacion(String ubicacion) {
        return inventarioRepository.findByUbicacion(ubicacion);
    }


}
