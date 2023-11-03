package com.edu.pe.ordermicroservice.orders.resources;

import com.edu.pe.ordermicroservice.shoppingcart.domain.model.ShoppingCart;
import com.edu.pe.ordermicroservice.users.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data()
@NoArgsConstructor
@AllArgsConstructor
public class OrderResource {
    private Long id;
    private LocalDate date;
    private String waitingTime;
    private Double totalPrice;
    private String orderStatus;
    private String paymentMethod;
    private Double paymentAmount;
    private User user;
    private ShoppingCart shoppingCart;
}
