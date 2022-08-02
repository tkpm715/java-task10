package com.raisetech.task10.controller;

public class UserDataResponse {
  private int id;
  private String name;
  private String postcode;
  private String address;

  //DTOクラス
  public UserDataResponse(int id, String name, String postcode, String address) {
    this.id = id;
    this.name = name;
    this.postcode = postcode;
    this.address = address;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPostcode() {
    return postcode;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}

