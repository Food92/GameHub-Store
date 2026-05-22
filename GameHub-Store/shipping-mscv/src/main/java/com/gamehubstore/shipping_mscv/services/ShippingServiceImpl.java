package com.gamehubstore.shipping_mscv.services;

import com.gamehubstore.shipping_mscv.clients.DireccionClient;
import com.gamehubstore.shipping_mscv.clients.OrderClient;
import com.gamehubstore.shipping_mscv.clients.UserClient;
import com.gamehubstore.shipping_mscv.exceptions.ShippingException;
import com.gamehubstore.shipping_mscv.models.Audit;
import com.gamehubstore.shipping_mscv.models.Shipping;
import com.gamehubstore.shipping_mscv.models.dtos.*;
import com.gamehubstore.shipping_mscv.repositories.AuditRepository;
import com.gamehubstore.shipping_mscv.repositories.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private DireccionClient direccionClient;

    @Autowired
    private AuditRepository auditRepository;


    @Transactional
    @Override
    public ShippingDTO save(ShippingDTO shippingDTO) {
        // ✅ Validar orden
        OrderDTO order = orderClient.findById(shippingDTO.getOrderId());
        if (order == null || !"PAGADA".equals(order.getEstado())) {
            throw new ShippingException("No se puede despachar una orden no pagada");
        }

        // ✅ Validar usuario
        UserDTO user = userClient.getUserById(shippingDTO.getUserId());
        if (user == null) {
            throw new ShippingException("Usuario no existe, no se puede generar despacho");
        }

        // ✅ Obtener dirección del usuario
        List<DireccionDTO> direcciones = direccionClient.getDireccionesByUserId(shippingDTO.getUserId());
        if (direcciones == null || direcciones.isEmpty()) {
            throw new ShippingException("El usuario no tiene dirección registrada");
        }
        DireccionDTO direccion = direcciones.get(0); // snapshot de la primera

        // ✅ Crear entidad Shipping
        Shipping shipping = new Shipping();
        shipping.setUserId(shippingDTO.getUserId());
        shipping.setOrderId(shippingDTO.getOrderId());
        shipping.setEstado("EN_DESPACHO");

        // Dirección snapshot segura
        String direccionCompleta = String.format("%s %s, %s, %s",
                Optional.ofNullable(direccion.getCalle()).orElse(""),
                Optional.ofNullable(direccion.getNumero()).orElse(""),
                Optional.ofNullable(direccion.getComuna()).orElse(""),
                Optional.ofNullable(direccion.getCiudad()).orElse("")
        );
        if (direccionCompleta.isBlank()) {
            throw new ShippingException("La dirección del usuario está vacía");
        }
        shipping.setDireccion(direccionCompleta);

        // Transportista obligatorio
        if (shippingDTO.getTransportista() == null || shippingDTO.getTransportista().isBlank()) {
            throw new ShippingException("Debe indicar transportista");
        }
        shipping.setTransportista(shippingDTO.getTransportista());

        // Tracking automático
        shipping.setTracking(UUID.randomUUID().toString());

        // ✅ Guardar en BD
        Shipping saved = shippingRepository.save(shipping);

        // ✅ Construir DTO de respuesta
        ShippingDTO dto = new ShippingDTO();
        dto.setShippingId(saved.getShippingId());
        dto.setUserId(saved.getUserId());
        dto.setOrderId(saved.getOrderId());
        dto.setEstado(saved.getEstado());
        dto.setDireccion(saved.getDireccion());
        dto.setTransportista(saved.getTransportista());
        dto.setTracking(saved.getTracking());

        return dto;
    }


    @Override
    public Shipping update(ShippingDTO shippingDTO, Long shippingId) {
        Shipping shipping = shippingRepository.findById(shippingId)
                .orElseThrow(() -> new ShippingException("No se encontró el envío"));

        String estadoAnterior = shipping.getEstado();
        shipping.setEstado(shippingDTO.getEstado());
        if (shippingDTO.getTracking() != null) {
            shipping.setTracking(shippingDTO.getTracking());
        }

        Shipping actualizado = shippingRepository.save(shipping);
        registrarAuditoria(actualizado, estadoAnterior, actualizado.getEstado());
        return actualizado;
    }

    @Override
    public void cancel(CancelShippingDTO cancelShippingDTO) {
        Shipping shipping = shippingRepository.findById(cancelShippingDTO.getShippingId())
                .orElseThrow(() -> new ShippingException("No se encontró el envío"));

        String estadoAnterior = shipping.getEstado();
        shipping.setEstado("CANCELADO");
        shippingRepository.save(shipping);
        registrarAuditoria(shipping, estadoAnterior, "CANCELADO");
    }

    @Override
    public List<Shipping> findAll() {
        return shippingRepository.findAll();
    }

    @Override
    public List<Shipping> findByUserId(Long userId) {
        return shippingRepository.findByUserId(userId);
    }

    @Override
    public List<Shipping> findByOrderId(Long orderId) {
        return shippingRepository.findByOrderId(orderId);
    }

    @Override
    public List<Shipping> findByEstado(String estado) {
        return shippingRepository.findByEstado(estado);
    }

    @Override
    public List<Audit> findAuditByShippingId(Long shippingId) {
        return auditRepository.findByshippingId(shippingId);
    }

    // Método privado para registrar auditoría
    private void registrarAuditoria(Shipping shipping, String estadoAnterior, String estadoNuevo) {
        Audit audit = new Audit();
        audit.setShippingId(shipping.getShippingId());
        audit.setEstadoAnterior(estadoAnterior);
        audit.setEstadoNuevo(estadoNuevo);
        audit.setFechaCambio(LocalDateTime.now());
        auditRepository.save(audit);
    }
}

