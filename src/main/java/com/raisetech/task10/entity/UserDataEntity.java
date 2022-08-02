package com.raisetech.task10.entity;

public class UserDataEntity {
  private int id;
  private String name;
  private String postcode;

  public UserDataEntity(int id, String name, String postcode) {
    this.id = id;
    this.name = name;
    this.postcode = postcode;
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

}
