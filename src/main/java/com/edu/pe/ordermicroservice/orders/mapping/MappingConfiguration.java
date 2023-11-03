package com.edu.pe.ordermicroservice.orders.mapping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("behaviourMappingConfiguration")
public class MappingConfiguration {
    @Bean
    public OrderMapper tripMapper() {
        return new OrderMapper();
    }
}
