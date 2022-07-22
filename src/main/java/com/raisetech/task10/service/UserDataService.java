package com.raisetech.task10.service;

import com.raisetech.task10.entity.UserDataEntity;
import java.util.List;

public interface UserDataService {
  List<UserDataEntity> findAllUserData();

  UserDataEntity findOneUserData(Integer id);

  void saveUserData(UserDataEntity userDataEntity);

  void updateUserData(UserDataEntity userDataEntity);

  void deleteUserData(Integer id);

  String fetchAddress(String postCode);
}
