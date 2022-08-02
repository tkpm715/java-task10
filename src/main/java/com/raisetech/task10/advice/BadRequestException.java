package com.raisetech.task10.advice;

//400BadRequest例外
public class BadRequestException extends RuntimeException {
  public BadRequestException(String message) {
    super(message);
  }
}
