package com.microservices.auth.exception;

public class ValidationException extends RuntimeException {
  public ValidationException(String message){
    super(message);
  }
}
