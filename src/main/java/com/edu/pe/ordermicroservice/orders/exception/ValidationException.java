package com.edu.pe.ordermicroservice.orders.exception;

public class ValidationException extends RuntimeException{
    public ValidationException() {

    }
    public ValidationException(String message) {
        super(message);
    }
}
