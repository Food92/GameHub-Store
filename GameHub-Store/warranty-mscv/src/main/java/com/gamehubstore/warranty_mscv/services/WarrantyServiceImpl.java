package com.gamehubstore.warranty_mscv.services;

import com.gamehubstore.warranty_mscv.exception.WarrantyException;
import com.gamehubstore.warranty_mscv.models.Warranty;
import com.gamehubstore.warranty_mscv.models.dtos.WarrantyCloseDTO;
import com.gamehubstore.warranty_mscv.repositories.WarrantyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WarrantyServiceImpl implements  WarrantyService {

    @Autowired
    private WarrantyRepository warrantyRepository;


    @Override
    public Warranty save(Warranty warranty) {
        // Validaciones: existencia de orden, producto, usuario, plazo de garantía
        warranty.setEstado("ABIERTA");
        warranty.setFechaSolicitud(LocalDateTime.now());
        return warrantyRepository.save(warranty);
    }


    @Override
    public Warranty update(Long warrantyId, Warranty warranty) {
        Warranty existente = warrantyRepository.findById(warrantyId)
                .orElseThrow(() -> new RuntimeException("Garantía no encontrada"));

        existente.setEstado(warranty.getEstado());
        existente.setResolucion(warranty.getResolucion());
        existente.setMotivo(warranty.getMotivo());
        // otros campos que quieras actualizar

        return warrantyRepository.save(existente);
    }




    @Override
    public Warranty findById(Long warrantyId) {
        return warrantyRepository.findById(warrantyId).orElseThrow(
                ()-> new WarrantyException("Warranty not find"));
    }



    @Override
    public Warranty closeWarranty(Long warrantyId, WarrantyCloseDTO request) {
        Warranty warranty = findById(warrantyId);

        if (request.getResolucion() == null || request.getResolucion().isBlank()) {
            throw new RuntimeException("No se puede cerrar garantía sin resolución");
        }

        warranty.setResolucion(request.getResolucion());
        warranty.setEstado(request.getEstado()); // debe ser "CERRADA"
        return warrantyRepository.save(warranty);
    }




    @Override
    public List<Warranty> findAllByProductId(Long productId) {
        return warrantyRepository.findByProductId(productId);
    }



    @Override
    public List<Warranty> findAllByUserId(Long userId) {
        return warrantyRepository.findByUserId(userId);
    }



    @Override
    public List<Warranty> findAllByEstado(String estado) {
        return warrantyRepository.findByEstado(estado);
    }


    @Override
    public List<Warranty> findAll() {
        return warrantyRepository.findAll();
    }

}
