package com.edu.pe.ordermicroservice.orders.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException() {}
    public InvalidRequestException(String message) { super(message); }
}
