package com.edu.pe.ordermicroservice.orders.exception;

public class InvalidCreateResourceException extends RuntimeException {
    public InvalidCreateResourceException() {

    }
    public InvalidCreateResourceException(String message) {
        super(message);
    }
}
