package com.edu.pe.ordermicroservice.shoppingcart.client;

import com.edu.pe.ordermicroservice.shoppingcart.domain.model.ShoppingCart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="${tripstore.shopping-carts-service.name}", path = "${tripstore.shopping-carts-service.path}")
public interface IShoppingCartClient {
    @GetMapping("orders/{id}")
    ResponseEntity<ShoppingCart> getShoppingCartByOrderId(@PathVariable Long id);

    @DeleteMapping(value = "orders/{id}")
    ResponseEntity<ShoppingCart> deleteShoppingCartByOrderId(@PathVariable Long id);
}
