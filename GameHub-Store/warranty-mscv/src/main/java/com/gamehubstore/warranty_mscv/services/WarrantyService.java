package com.gamehubstore.warranty_mscv.services;

import com.gamehubstore.warranty_mscv.models.Warranty;
import com.gamehubstore.warranty_mscv.models.dtos.WarrantyCloseDTO;

import java.util.List;

public interface WarrantyService {
    Warranty save(Warranty warranty);
    Warranty update(Long warrantyId, Warranty warranty);
    Warranty findById(Long warrantyId);
    List<Warranty> findAll();
    List<Warranty> findAllByUserId(Long userId);
    List<Warranty> findAllByProductId(Long productId);
    List<Warranty> findAllByEstado(String estado);
    Warranty closeWarranty(Long warrantyId, WarrantyCloseDTO request);
}


