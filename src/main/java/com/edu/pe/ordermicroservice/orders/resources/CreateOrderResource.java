package com.edu.pe.ordermicroservice.orders.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResource {
    @NotBlank
    private String waitingTime;

    @Positive
    @NotNull
    private Double totalPrice;

    @NotBlank
    private String paymentMethod;

    @Positive
    @NotNull
    private Double paymentAmount;

    @Positive
    @NotNull
    private Long userId;
}
