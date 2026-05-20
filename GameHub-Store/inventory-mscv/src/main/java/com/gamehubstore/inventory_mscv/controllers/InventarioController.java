package com.gamehubstore.inventory_mscv.controllers;

import com.gamehubstore.inventory_mscv.models.Inventario;
import com.gamehubstore.inventory_mscv.services.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventaries")

public class InventarioController {
    @Autowired
    InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<Inventario>> listarInventarios() {
        return ResponseEntity.ok(inventarioService.findAll());
    }


    // Crear inventario
    @PostMapping
    public ResponseEntity<Inventario> save(@RequestBody Inventario inventario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.save(inventario));
    }

    // Actualizar inventario
    @PutMapping("/{id}")
    public ResponseEntity<Inventario> update(@PathVariable Long id,
                                                           @RequestBody Inventario inventario) {
        return ResponseEntity.ok(inventarioService.update(inventario, id));
    }

    // Reservar stock
    @PostMapping("/{id}/reservar")
    public ResponseEntity<Inventario> reservarStock(@PathVariable Long id,
                                                    @RequestParam Long cantidad) {
        return ResponseEntity.ok(inventarioService.reservarStock(id, cantidad));
    }

    // Eliminar inventario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        inventarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar inventarios por producto
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<Inventario>> buscarPorProducto(@PathVariable Long idProducto) {
        return ResponseEntity.ok(inventarioService.findByProducto(idProducto));
    }

    // Buscar inventarios por ubicación
    @GetMapping("/ubicacion/{ubicacion}")
    public ResponseEntity<List<Inventario>> buscarPorUbicacion(@PathVariable String ubicacion) {
        return ResponseEntity.ok(inventarioService.findByUbicacion(ubicacion));
    }


    // Salida de stock
    @PostMapping("/{id}/salida")
    public ResponseEntity<Inventario> salidaStock(@PathVariable Long id, @RequestParam Long cantidad) {
        return ResponseEntity.ok(inventarioService.salidaStock(id, cantidad));
    }

}
