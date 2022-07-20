package com.raisetech.task10.advice;

public class NotFoundException extends RuntimeException{

  public NotFoundException(String message) {
    super(message);
  }
}
