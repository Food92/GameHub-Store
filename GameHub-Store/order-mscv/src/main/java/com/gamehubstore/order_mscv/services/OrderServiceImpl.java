package com.gamehubstore.order_mscv.services;

import com.gamehubstore.order_mscv.exceptions.OrderException;
import com.gamehubstore.order_mscv.models.DetailOrder;
import com.gamehubstore.order_mscv.models.Order;
import com.gamehubstore.order_mscv.models.dtos.DetailOrderDTO;
import com.gamehubstore.order_mscv.models.dtos.OrderDTO;
import com.gamehubstore.order_mscv.repositories.DetailOrderRepository;
import com.gamehubstore.order_mscv.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DetailOrderRepository detailOrderRepository;

    @Override
    public OrderDTO save(OrderDTO orderDTO) {
        Order order = new Order();
        order.setUserId(orderDTO.getUserId());
        order.setEstado("CREADA");
        order.setFecha(LocalDateTime.now());

        // Calcular subtotal
        double subtotal = orderDTO.getDetails().stream()
                .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                .sum();
        order.setSubtotal(subtotal);

        // Aplicar descuento
        double descuento = orderDTO.getDescuento() != null ? orderDTO.getDescuento() : 0.0;
        order.setDescuento(descuento);

        // Calcular total
        order.setTotal(subtotal - descuento);

        // Guardar orden
        Order saved = orderRepository.save(order);

        // Guardar detalles
        for (DetailOrderDTO detailOrderDTO : orderDTO.getDetails()) {
            DetailOrder detailOrder = new DetailOrder();
            detailOrder.setOrderId(saved.getOrderId());
            detailOrder.setProductId(detailOrderDTO.getProductId());
            detailOrder.setCantidad(detailOrderDTO.getCantidad());
            detailOrder.setPrecioUnitario(detailOrderDTO.getPrecioUnitario());
            detailOrderRepository.save(detailOrder);
        }

        // ✅ Construir DTO de respuesta
        OrderDTO response = new OrderDTO();
        response.setOrderId(saved.getOrderId());
        response.setUserId(saved.getUserId());
        response.setEstado(saved.getEstado());
        response.setSubtotal(saved.getSubtotal());
        response.setDescuento(saved.getDescuento());
        response.setTotal(saved.getTotal());

        // Convertir detalles a DTO
        List<DetailOrderDTO> detalles = detailOrderRepository.findByOrderId(saved.getOrderId())
                .stream()
                .map(d -> {
                    DetailOrderDTO detDto = new DetailOrderDTO();
                    detDto.setProductId(d.getProductId());
                    detDto.setCantidad(d.getCantidad());
                    detDto.setPrecioUnitario(d.getPrecioUnitario());
                    return detDto;
                })
                .toList();

        response.setDetails(detalles);

        return response;
    }


    @Override
    public Order update(OrderDTO dto, Long orderId) {
        Order existente = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Orden no encontrada con ID: " + orderId));

        if ("PAGADA".equals(existente.getEstado())) {
            throw new OrderException("No se puede modificar una orden ya pagada");
        }

        // Recalcular subtotal
        double subtotal = dto.getDetails().stream()
                .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                .sum();
        existente.setSubtotal(subtotal);

        // Aplicar descuento
        double descuento = dto.getDescuento() != null ? dto.getDescuento() : 0.0;
        existente.setDescuento(descuento);

        // Calcular total
        existente.setTotal(subtotal - descuento);

        // Actualizar estado si viene en el DTO
        if (dto.getEstado() != null) {
            existente.setEstado(dto.getEstado());
        }

        return orderRepository.save(existente);
    }




    @Override
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Orden no encontrada con ID: " + orderId));
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> findByEstado(String estado) {
        return orderRepository.findByEstado(estado);
    }

    @Override
    public Order cancelOrder(Long orderId) {
        Order orden = findById(orderId);

        if ("PAGADA".equals(orden.getEstado())) {
            throw new OrderException("No se puede cancelar una orden ya pagada");
        }

        orden.setEstado("CANCELADA");
        return orderRepository.save(orden);
    }

    @Override
    public OrderDTO findByIdDTO(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Orden no encontrada con ID: " + orderId));

        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUserId());
        dto.setDescuento(order.getDescuento());
        dto.setEstado(order.getEstado());
        dto.setSubtotal(order.getSubtotal()); // ✅ agregado
        dto.setTotal(order.getTotal());       // ✅ agregado

        // Convertir detalles a DTO
        List<DetailOrderDTO> detalles = detailOrderRepository.findByOrderId(order.getOrderId())
                .stream()
                .map(d -> {
                    DetailOrderDTO detDto = new DetailOrderDTO();
                    detDto.setProductId(d.getProductId());
                    detDto.setCantidad(d.getCantidad());
                    detDto.setPrecioUnitario(d.getPrecioUnitario());
                    return detDto;
                })
                .toList();

        dto.setDetails(detalles);
        return dto;
    }


    @Override
    public OrderDTO updateEstado(Long orderId, String estado) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Orden no encontrada"));

        order.setEstado(estado);
        Order actualizado = orderRepository.save(order);

        // ✅ Crear el DTO
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(actualizado.getOrderId());
        dto.setUserId(actualizado.getUserId());
        dto.setEstado(actualizado.getEstado());
        dto.setSubtotal(actualizado.getSubtotal());
        dto.setDescuento(actualizado.getDescuento());
        dto.setTotal(actualizado.getTotal());

        // 🔴 Cargar los detalles desde el repositorio
        List<DetailOrder> detalles = detailOrderRepository.findByOrderId(actualizado.getOrderId());

        List<DetailOrderDTO> detallesDTO = detalles.stream()
                .map(d -> {
                    DetailOrderDTO dtoDet = new DetailOrderDTO();
                    dtoDet.setProductId(d.getProductId());
                    dtoDet.setCantidad(d.getCantidad());
                    dtoDet.setPrecioUnitario(d.getPrecioUnitario());
                    return dtoDet;
                })
                .toList();

        dto.setDetails(detallesDTO);

        // ✅ Return final
        return dto;
    }



}
