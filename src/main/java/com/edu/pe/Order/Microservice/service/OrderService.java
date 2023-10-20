package com.edu.pe.Order.Microservice.service;

import com.edu.pe.Order.Microservice.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(int id);
    Order saveOrder(Order order);
    Order updateOrder(int id, Order order);
    void deleteOrder(int id);
}
