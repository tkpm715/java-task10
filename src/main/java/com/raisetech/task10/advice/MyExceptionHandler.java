package com.raisetech.task10.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {
  //400BadRequest例外
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> handleBadRequestException(
      BadRequestException exception, WebRequest request) {
    HttpHeaders headers = new HttpHeaders();

    return super.handleExceptionInternal(exception,
        createBadRequestErrorResponseBody(exception, request),
        headers,
        HttpStatus.BAD_REQUEST,
        request);
  }

  private Object createBadRequestErrorResponseBody(
      BadRequestException exception, WebRequest request) {
    ErrorResponseBody errorResponseBody = new ErrorResponseBody();
    int responseCode = HttpStatus.BAD_REQUEST.value();
    String responseErrorMessage = HttpStatus.BAD_REQUEST.getReasonPhrase();
    String uri = ((ServletWebRequest) request).getRequest().getRequestURI();

    errorResponseBody.setStatus(responseCode);
    errorResponseBody.setError(responseErrorMessage);
    errorResponseBody.setMessage(exception.getMessage());

    return errorResponseBody;
  }


  //404NotFound例外
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Object> handledNotFoundException(
      NotFoundException exception, WebRequest request) {
    HttpHeaders headers = new HttpHeaders();

    return super.handleExceptionInternal(exception,
        createNotFoundErrorResponseBody(exception, request),
        headers,
        HttpStatus.NOT_FOUND,
        request);
  }

  private Object createNotFoundErrorResponseBody(NotFoundException exception,
                                                 WebRequest request) {
    ErrorResponseBody errorResponseBody = new ErrorResponseBody();
    int responseCode = HttpStatus.NOT_FOUND.value();
    String responseErrorMessage = HttpStatus.NOT_FOUND.getReasonPhrase();
    String uri = ((ServletWebRequest) request).getRequest().getRequestURI();

    errorResponseBody.setStatus(responseCode);
    errorResponseBody.setError(responseErrorMessage);
    errorResponseBody.setMessage(exception.getMessage());

    return errorResponseBody;
  }

}

