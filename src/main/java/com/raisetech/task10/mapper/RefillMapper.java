package com.raisetech.task10.mapper;

import com.raisetech.task10.controller.UserDataResponse;
import com.raisetech.task10.entity.UserDataEntity;
import com.raisetech.task10.form.UserDataForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RefillMapper {
  //formをentityに詰める
  UserDataEntity useDataFormToUserDataEntity(UserDataForm userDataForm);

  //entityをresponseに詰める（responseのaddressフィールドは除く）
  @Mapping(target = "address", ignore = true)
  UserDataResponse useDataEntityToUserDataResponse(
      UserDataEntity userDataEntity);

}
