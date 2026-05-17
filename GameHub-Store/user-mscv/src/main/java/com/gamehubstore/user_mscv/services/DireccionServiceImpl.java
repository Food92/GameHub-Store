package com.gamehubstore.user_mscv.services;

import com.gamehubstore.user_mscv.models.Direccion;
import com.gamehubstore.user_mscv.models.dtos.DireccionDTO;
import com.gamehubstore.user_mscv.repositories.DireccionReposittory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DireccionServiceImpl implements DireccionService{

    @Autowired
    private DireccionReposittory direccionReposittory;

    @Override
    public DireccionDTO save(DireccionDTO direccionDTO) {
        Direccion direccion = new Direccion();
        direccion.setUserId(direccionDTO.getUserId());
        direccion.setComuna( direccionDTO.getComuna());
        direccion.setCiudad( direccionDTO.getCiudad());
        direccion.setCalle( direccionDTO.getCalle());
        direccion.setNumero( direccionDTO.getNumero());

        Direccion saved = direccionReposittory.save(direccion);

        DireccionDTO dto = new DireccionDTO();
        dto.setDireccionId(saved.getDireccionId());
        dto.setUserId( saved.getUserId());
        dto.setComuna(saved.getComuna());
        dto.setCiudad(saved.getCiudad());
        dto.setCalle(saved.getCalle());
        dto.setNumero(saved.getNumero());

        return dto;
    }

    @Override
    public List<DireccionDTO> findByUserId(Long id) {
        return direccionReposittory.findByUserId(id)
                .stream()
                .map(d->{
                    DireccionDTO dto = new DireccionDTO();
                    dto.setDireccionId(d.getDireccionId());
                    dto.setUserId( d.getUserId());
                    dto.setComuna(d.getComuna());
                    dto.setCiudad(d.getCiudad());
                    dto.setCalle(d.getCalle());
                    dto.setNumero(d.getNumero());
                    return dto;
                })
                .toList();
    }



    @Override
    public void delete(Long id) {
        if (!direccionReposittory.existsById(id)) {
            throw new RuntimeException("Dirección no encontrada");
        }
        direccionReposittory.deleteById(id);
    }

}
