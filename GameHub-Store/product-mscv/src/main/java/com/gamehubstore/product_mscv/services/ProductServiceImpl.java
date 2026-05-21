package com.gamehubstore.product_mscv.services;

import com.gamehubstore.product_mscv.client.CategoryClient;
import com.gamehubstore.product_mscv.exceptions.ProductException;
import com.gamehubstore.product_mscv.models.Product;
import com.gamehubstore.product_mscv.models.dtos.CategoryDTO;
import com.gamehubstore.product_mscv.models.dtos.ProductDTO;
import com.gamehubstore.product_mscv.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryClient categoryClient;




    @Transactional(readOnly = true)
    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream().map(p -> {
            ProductDTO dto = new ProductDTO();
            dto.setIdProduct(p.getIdProduct());
            dto.setIdCategory(p.getIdCategory());
            dto.setNombreProduct(p.getNombreProduct());
            dto.setModelo(p.getModelo());
            dto.setPrecio(p.getPrecio());
            dto.setMarca(p.getMarca());
            dto.setDescripcion(p.getDescripcion());
            dto.setEstado(p.getEstado());
            return dto;
        }).toList();
    }





    @Transactional(readOnly = true)
    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductException("El producto con el id: " + id + " no existe"));
    }



    @Transactional
    @Override
    public Product save(Product product) {
        // Validar duplicado por nombre
        if (productRepository.existsByNombreProduct(product.getNombreProduct())) {
            throw new ProductException(
                    "El producto '" + product.getNombreProduct() + "' ya está registrado en el catálogo. " +
                            "Por favor, utilice otro nombre o edite el existente."
            );
        }


        // Validar precio
        if (product.getPrecio() == null || product.getPrecio() <= 0) {
            throw new ProductException("El precio del producto debe ser mayor a 0");
        }

        // Validar categoría
        if (product.getIdCategory() == null) {
            throw new ProductException("El producto debe tener una categoría asignada");
        }

        // Consultar categoría vía CategoryClient
        CategoryDTO dto = categoryClient.findById(product.getIdCategory());
        if (dto == null) {
            throw new ProductException("La categoría no existe en el sistema");
        }

        // Validar estado de la categoría
        if ("INACTIVA".equalsIgnoreCase(dto.getEstado())) {
            throw new ProductException("No se puede asignar producto a una categoría inactiva");
        }

        // Validar marca y modelo
        if (product.getMarca() == null || product.getMarca().isBlank()) {
            throw new ProductException("El campo marca no puede estar vacío");
        }
        if (product.getModelo() == null || product.getModelo().isBlank()) {
            throw new ProductException("El campo modelo no puede estar vacío");
        }

        // Validar descripción
        if (product.getDescripcion() == null || product.getDescripcion().isBlank()) {
            throw new ProductException("La descripción del producto no puede estar vacía");
        }

        // Validar estado (activo/inactivo)
        if (product.getEstado() == null) {
            throw new ProductException("El estado del producto no puede ser nulo");
        }

        // Guardar producto
        return productRepository.save(product);
    }





    @Transactional
    @Override
    public Product update(Product product, Long id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("El producto con id " + id + " no existe"));

        if (product.getPrecio() != null && product.getPrecio() <= 0) {
            throw new ProductException("El precio debe ser mayor a cero");
        }
        existing.setNombreProduct(product.getNombreProduct());
        existing.setMarca(product.getMarca());
        existing.setModelo(product.getModelo());
        existing.setPrecio(product.getPrecio());
        existing.setDescripcion(product.getDescripcion());
        existing.setEstado(product.getEstado());
        return productRepository.save(existing);
    }




    @Transactional
    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("El producto con id " + id + " no existe"));
        productRepository.delete(product);
    }





    @Transactional
    @Override
    public void desactivar(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("El producto con id " + id + " no existe"));

        product.setEstado(Product.EstadoProducto.INACTIVO); // 👈 ahora usas el enum
        productRepository.save(product);
    }




        @Transactional(readOnly = true)
    @Override
    public List<Product> findByMarca(String marca) {
        return productRepository.findByMarca(marca);
    }




    @Transactional(readOnly = true)
    @Override
    public List<Product> findByIdCategory(Long idCategory) {
        return productRepository.findByIdCategory(idCategory);
    }




    @Transactional(readOnly = true)
    @Override
    public List<Product> findByEstado(Boolean estado) {
        return productRepository.findByEstado(estado);
    }
}

