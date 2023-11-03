package com.edu.pe.ordermicroservice.orders.persistence.repository;

import com.edu.pe.ordermicroservice.orders.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByUserId(Long userId);
    List<Order> deleteOrdersByUserId(Long userId);
}
