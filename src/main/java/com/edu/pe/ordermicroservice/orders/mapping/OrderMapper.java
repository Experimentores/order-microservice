package com.edu.pe.ordermicroservice.orders.mapping;

import com.crudjpa.mapping.IEntityMapper;
import com.edu.pe.ordermicroservice.mapping.EnhancedModelMapper;
import com.edu.pe.ordermicroservice.orders.domain.model.Order;
import com.edu.pe.ordermicroservice.orders.resources.CreateOrderResource;
import com.edu.pe.ordermicroservice.orders.resources.UpdateOrderResource;
import com.edu.pe.ordermicroservice.orders.resources.OrderResource;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderMapper implements IEntityMapper<Order, OrderResource, CreateOrderResource, UpdateOrderResource> {
    @Autowired
    EnhancedModelMapper mapper;
    @Override
    public Order fromCreateResourceToModel(CreateOrderResource scoreResource) {
        return mapper.map(scoreResource, Order.class);
    }

    @Override
    public void fromCreateResourceToModel(CreateOrderResource resource, Order user) {
        mapper.map(resource, user);
    }

    @Override
    public OrderResource fromModelToResource(Order score) {
        return mapper.map(score, OrderResource.class);
    }

    @Override
    public Order fromUpdateResourceToModel(UpdateOrderResource user) {
        return mapper.map(user, Order.class);
    }

    @Override
    public void fromUpdateResourceToModel(UpdateOrderResource updateUserResource, Order user) {
        mapper.map(updateUserResource, user);
    }
}
