package com.raisetech.task10.form;


import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDataForm {
  @Pattern(regexp = "^\\d+$", message = "idは数値を入力してください")
  private int id;
  @Size(max = 50, message = "氏名は50文字以下に設定してください")
  private String name;
  @Pattern(regexp = "\\d{7}", message = "郵便番号は半角数字7桁に設定してください。")
  private String postcode;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
