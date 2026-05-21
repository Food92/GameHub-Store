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
    public Order save(OrderDTO orderDTO) {
        Order order = new Order();
        order.setUserId(orderDTO.getUserId());
        order.setEstado("CREADA");
        order.setFecha(LocalDateTime.now());

        //Calcular subtotal
        double subtotal =orderDTO.getDetails().stream()
                .mapToDouble(
                        d-> d.getCantidad() * d.getPrecioUnitario())
                .sum();
        order.setSubtotal(subtotal);


        // Aplicar descuento
        double descuento= orderDTO.getDescuento() !=null ? orderDTO.getDescuento() :0.0;
        order.setDescuento(descuento);

        //Calcular total
        order.setTotal(subtotal - descuento);

        Order saved = orderRepository.save(order);

        // Guardar detalles
        Order sav = orderRepository.save(order);

        for (DetailOrderDTO detailOrderDTO : orderDTO.getDetails()) {
            DetailOrder detailOrder = new DetailOrder();
            detailOrder.setOrderId(sav.getOrderId()); // correcto porque tu campo es orderId
            detailOrder.setProductId(detailOrderDTO.getProductId());
            detailOrder.setCantidad(detailOrderDTO.getCantidad());
            detailOrder.setPreciUnitario(detailOrderDTO.getPrecioUnitario()); // corregido
            detailOrderRepository.save(detailOrder);
        }

        return sav;
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
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + orderId));
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
            throw new RuntimeException("No se puede cancelar una orden ya pagada");
        }

        orden.setEstado("CANCELADA");
        // Aquí podrías liberar stock en inventory-service
        return orderRepository.save(orden);
    }

}
