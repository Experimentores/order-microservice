package com.edu.pe.Order.Microservice.repository;

import com.edu.pe.Order.Microservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
