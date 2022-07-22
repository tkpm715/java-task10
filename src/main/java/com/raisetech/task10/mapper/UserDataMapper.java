package com.raisetech.task10.mapper;

import com.raisetech.task10.entity.UserDataEntity;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDataMapper {
  @Select("select * from users")
  List<UserDataEntity> findAllUserData();

  @Select("select * from users where id=#{id}")
  Optional<UserDataEntity> findOneUserData(Integer id);

  @Insert("insert into users (name, postcode) values (#{name}, #{postcode})")
  @Options(useGeneratedKeys = true)
  void saveUserData(UserDataEntity userDataEntity);

  @Update("update users set name=#{name}, postcode=#{postcode} where id=#{id}")
  void updateUserData(UserDataEntity userDataEntity);

  @Delete("delete from users where id=#{id}")
  void deleteUserData(Integer id);
}

