package com.gamehubstore.inventory_mscv.controllers;

import com.gamehubstore.inventory_mscv.models.Inventario;
import com.gamehubstore.inventory_mscv.services.InventarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement; // 👈 Importar
import io.swagger.v3.oas.annotations.tags.Tag;               // 👈 Importar
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventaries")
@Tag(name = "Inventarios", description = "Gestión de stock físico, reservas y despacho de videojuegos") // 👈 Agrupa los endpoints en Swagger
@SecurityRequirement(name = "bearer-jwt")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    // Cualquier usuario autenticado (Cliente, Vendedor, Admin) puede consultar stock
    @GetMapping
    public ResponseEntity<List<Inventario>> listarInventarios() {
        return ResponseEntity.ok(inventarioService.findAll());
    }

    // CREAR INVENTARIO: Exclusivo de perfil administrativo/vendedor
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')") // 👈 Corrección: Permite el ingreso del Vendedor
    public ResponseEntity<Inventario> save(@RequestBody Inventario inventario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.save(inventario));
    }

    // ACTUALIZAR INVENTARIO: Ajustes físicos de bodega
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')") // 👈 Corrección: Permite el ingreso del Vendedor
    public ResponseEntity<Inventario> update(@PathVariable Long id, @RequestBody Inventario inventario) {
        return ResponseEntity.ok(inventarioService.update(inventario, id));
    }

    // RESERVAR STOCK: Generalmente lo invoca de forma interna el microservicio de órdenes
    @PostMapping("/{id}/reservar")
    public ResponseEntity<Inventario> reservarStock(@PathVariable Long id, @RequestParam Long cantidad) {
        return ResponseEntity.ok(inventarioService.reservarStock(id, cantidad));
    }

    // ELIMINAR REGISTROS DE STOCK: Acción crítica reservada solo para el administrador
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // 👈 El vendedor no debe poder borrar registros completos de inventario
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        inventarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<Inventario>> buscarPorProducto(@PathVariable Long idProducto) {
        return ResponseEntity.ok(inventarioService.findByProducto(idProducto));
    }

    @GetMapping("/ubicacion/{ubicacion}")
    public ResponseEntity<List<Inventario>> buscarPorUbicacion(@PathVariable String ubicacion) {
        return ResponseEntity.ok(inventarioService.findByUbicacion(ubicacion));
    }

    // SALIDA DE STOCK (REDUCCIÓN POR COMPRA): Confirmada por el Vendedor al despachar
    @PostMapping("/{id}/salida")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')") // 👈 Corrección para la fase de despacho del Vendedor
    public ResponseEntity<Inventario> salidaStock(@PathVariable Long id, @RequestParam Long cantidad) {
        return ResponseEntity.ok(inventarioService.salidaStock(id, cantidad));
    }
}