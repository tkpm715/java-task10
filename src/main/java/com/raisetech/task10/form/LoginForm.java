package com.raisetech.task10.form;

import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginForm {
  private String loginId;
  private String pass;

  public String getLoginId() {
    return loginId;
  }

  public String getPass() {
    return pass;
  }

  public void encrypt(PasswordEncoder encoder){
    this.pass = encoder.encode(pass);
  }

  @Override
  public String toString() {
    return "UserForm{" +
        "loginId='" + loginId + '\'' +
        ", pass='" + pass + '\'' +
        '}';
  }
}
