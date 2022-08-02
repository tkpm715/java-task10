package com.raisetech.task10.service;

import com.raisetech.task10.controller.UserDataResponse;
import com.raisetech.task10.form.UserDataForm;
import java.util.List;

public interface UserDataService {
  List<UserDataResponse> findAllUserData();

  UserDataResponse findOneUserData(int id);

  void saveUserData(UserDataForm userDataForm);

  void updateUserData(UserDataForm userDataForm,int id);

  void deleteUserData(Integer id);

  String fetchAddress(String postCode);
}
