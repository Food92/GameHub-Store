package com.gamehubstore.user_mscv.services;

import com.gamehubstore.user_mscv.exceptions.UserException;
import com.gamehubstore.user_mscv.models.User;
import com.gamehubstore.user_mscv.models.dtos.UserDTO;
import com.gamehubstore.user_mscv.repositories.UserRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepositorio userRepositorio;

    public UserDTO save(UserDTO userDTO) {
        if (userRepositorio.existsByCorreo(userDTO.getCorreo())) {
            throw new UserException("Email ya está registrado");
        }

        User newUser = new User();
        newUser.setRut(userDTO.getRut());
        newUser.setNombreCompleto(userDTO.getNombreCompleto());
        newUser.setApellidoCompleto(userDTO.getApellidoCompleto());
        newUser.setCorreo(userDTO.getCorreo());
        newUser.setTelefono(userDTO.getTelefono());
        newUser.setEstado(userDTO.getEstado());

        User savedUser = userRepositorio.save(newUser);

        UserDTO dto = new UserDTO();
        dto.setUserId(savedUser.getUserId());
        dto.setRut(savedUser.getRut());
        dto.setNombreCompleto(savedUser.getNombreCompleto());
        dto.setApellidoCompleto(savedUser.getApellidoCompleto());
        dto.setCorreo(savedUser.getCorreo());
        dto.setTelefono(savedUser.getTelefono());
        dto.setEstado(savedUser.getEstado());
        return dto;

    }


    @Override
    public User findById(Long id) {
        // Buscar usuario por ID o lanzar excepción si no existe
        return userRepositorio.findById(id).orElseThrow(
                () -> new UserException("Usuario no encontrado"));
    }


    @Override
    public User update(Long id, User user) {
        User updateUser = userRepositorio.findById(id).orElseThrow(
                ()-> new UserException("Usuario no encontrado"));

        // Validar email único (excepto si es el mismo usuario)
        if(!updateUser.getCorreo().equals(user.getCorreo()) &&
        userRepositorio.existsByCorreo(user.getCorreo())) {
            throw new UserException("Usuario no encontrado");
        }

        // Actualizar campos
        updateUser.setRut(user.getRut());
        updateUser.setNombreCompleto(user.getNombreCompleto());
        updateUser.setApellidoCompleto(user.getApellidoCompleto());
        updateUser.setCorreo(user.getCorreo());
        updateUser.setTelefono(user.getTelefono());
        updateUser.setEstado(user.getEstado());

        return userRepositorio.save(updateUser);
    }


    @Override
    public void delete(Long id) {
        // Eliminar usuario si existe
        if(userRepositorio.existsById(id)) {
            throw new UserException("Usuario no encontrado");
        }
        userRepositorio.deleteById(id);
    }


    @Override
    public void desactivar(Long id) {
        User findUser = userRepositorio.findById(id).orElseThrow(
                ()-> new UserException("Usuario no encontrado"));
        findUser.setEstado(false);
        userRepositorio.save(findUser);
    }


    @Override
    public List<User> findAll() {
        return userRepositorio.findAll();
    }

    @Override
    public List<User> findByEstadoTrue() {
        // Listar usuarios activos
        return userRepositorio.findByEstadoTrue();
    }

    @Override
    public List<User> findByEstadoFalse() {
        // Listar usuarios inactivos
        return userRepositorio.findByEstadoFalse();
    }
}
