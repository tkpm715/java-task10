package com.raisetech.task10.service;


import com.raisetech.task10.entity.UserDataEntity;
import java.util.List;

public interface UserDataService {
  public List<UserDataEntity> findAll();

  public UserDataEntity findOne(Integer id);

  public void save (UserDataEntity userDataEntity);

  public void update(UserDataEntity userDataEntity);

  public void delete(Integer id);

  public String fetchAddress(String postCode);
}
