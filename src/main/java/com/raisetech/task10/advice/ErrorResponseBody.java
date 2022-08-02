package com.raisetech.task10.advice;

public class ErrorResponseBody {
  private int status;
  private String error;
  private String message;

  public ErrorResponseBody(int status, String error, String message) {
    this.status = status;
    this.error = error;
    this.message = message;
  }

  public int getStatus() {
    return status;
  }

  public String getError() {
    return error;
  }

  public String getMessage() {
    return message;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setError(String error) {
    this.error = error;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
