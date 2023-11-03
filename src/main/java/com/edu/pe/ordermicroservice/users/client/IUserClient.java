package com.edu.pe.ordermicroservice.users.client;

import com.edu.pe.ordermicroservice.users.domain.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="${tripstore.users-service.name}",
        path = "${tripstore.users-service.path}")
public interface IUserClient {
    @GetMapping("{id}")
    ResponseEntity<User> getUserById(@PathVariable Long id);
}