package com.edu.pe.ordermicroservice.orders.service;

import com.crudjpa.service.impl.CrudService;
import com.edu.pe.ordermicroservice.orders.domain.model.Order;
import com.edu.pe.ordermicroservice.orders.domain.services.IOrderService;
import com.edu.pe.ordermicroservice.orders.persistence.repository.IOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class OrderService extends CrudService<Order, Long> implements IOrderService {
    private final IOrderRepository repository;
    public OrderService(IOrderRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        return repository.findOrdersByUserId(userId);
    }

    @Override
    @Transactional
    public List<Order> deleteOrdersByUserId(Long userId) {
        return repository.deleteOrdersByUserId(userId);
    }
}
