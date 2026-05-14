package com.gamehubstore.product_mscv.services;

import com.gamehubstore.product_mscv.exceptions.ProductException;
import com.gamehubstore.product_mscv.models.Product;
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

    @Transactional(readOnly = true)
    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream().map(p -> {
            ProductDTO dto = new ProductDTO();
            dto.setIdProduct(p.getIdProduct());
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
        if (productRepository.existeByName(product.getNombreProduct())) {
            throw new ProductException("Ya existe un producto con el nombre: " + product.getNombreProduct());
        }

        // Validar precio
        if (product.getPrecio() == null || product.getPrecio() <= 0) {
            throw new ProductException("El precio del producto debe ser mayor a 0");
        }

        // Validar categoría
        if (product.getIdCategory() == null) {
            throw new ProductException("El producto debe tener una categoría asignada");
        }

        return productRepository.save(product);
    }

    @Transactional
    @Override
    public Product update(Product product, Long id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductException("El producto con id " + id + " no existe"));

        if (product.getPrecio() != null && product.getPrecio() <= 0) {
            throw new ProductException("El precio debe ser mayor a 0");
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
        product.setEstado(false);
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

