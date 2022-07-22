package com.raisetech.task10.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDataForm {
  //Formクラス。入力情報に対するバリデーション操作。
  private int id;
  @Size(min = 1, max = 50, message = "名前は1～50文字に設定してください")
  private String name;
  @Pattern(regexp = "\\d{7}", message = "郵便番号は半角数字7桁に設定してください。")
  private String postcode;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

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
}
