package com.raisetech.task10.mapper;

import com.raisetech.task10.controller.UserDataResponse;
import com.raisetech.task10.entity.UserDataEntity;
import com.raisetech.task10.form.UserDataForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;


@Mapper
public interface RefillMapper {

    UserDataEntity useDataFormToUserDataEntity(UserDataForm userDataForm);
    @Mapping(target = "address", ignore = true)
    UserDataResponse useDataEntityToUserDataResponse(UserDataEntity userDataEntity);

}
