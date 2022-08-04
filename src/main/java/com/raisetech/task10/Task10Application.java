package com.raisetech.task10;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(WebSecurityConfig.class)
public class Task10Application {
  public static void main(String[] args) {
    SpringApplication.run(Task10Application.class, args);
  }
}
