package com.raisetech.task10.advice;

//404NotFound例外
public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    super(message);
  }
}
