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

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }
}
