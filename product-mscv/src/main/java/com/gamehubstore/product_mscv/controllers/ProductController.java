package com.gamehubstore.product_mscv.controllers;

import com.gamehubstore.product_mscv.models.Product;
import com.gamehubstore.product_mscv.models.dtos.ProductDTO;
import com.gamehubstore.product_mscv.repositories.ProductRepository;
import com.gamehubstore.product_mscv.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
   private ProductService productService;

    //Crear un producto
    @PostMapping
    public ResponseEntity<Product> save(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    //Listar los productos
    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    //Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    //Listar por category
    @GetMapping("/category/{idCategory}")
    public ResponseEntity<List<Product>> findByCategory(@PathVariable Long idCategory) {
        return ResponseEntity.ok(productService.findByIdCategory(idCategory));
    }

    //Listar por marca
    @GetMapping("/marca/{marca}")
    public ResponseEntity<List<Product>> findByMarca(@PathVariable String marca) {
        return ResponseEntity.ok(productService.findByMarca(marca));
    }


    //Listar estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Product>>  findByEstado(@PathVariable Boolean estado) {
        return ResponseEntity.ok(productService.findByEstado(estado));
    }

    //Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.update(product, id));
    }

    //Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> delete(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }

    //Desactivar prducto
    @PatchMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id){
        productService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

}
