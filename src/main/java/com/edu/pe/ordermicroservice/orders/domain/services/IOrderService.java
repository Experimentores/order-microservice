package com.edu.pe.ordermicroservice.orders.domain.services;

import com.crudjpa.service.ICrudService;
import com.edu.pe.ordermicroservice.orders.domain.model.Order;

import java.util.List;

public interface IOrderService extends ICrudService<Order, Long> {
    List<Order> findOrdersByUserId(Long userId);
    List<Order> deleteOrdersByUserId(Long userId);
}
