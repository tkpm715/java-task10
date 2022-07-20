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
//  @Select("select * from movie where year_of_production=#{yearOfProduction}")
//  List<UserData> findOne(String yearOfProduction);

//  @Select("select exists(select * from user where id=#{id})")
//  Integer recordCheck(Integer id);

  @Select("select * from users")
  List<UserDataEntity> findAll();

  //１件取得
  @Select("select * from users where id=#{id}")
  Optional<UserDataEntity> findOne(Integer id);

  /* ユーザー 登録*/
  @Insert("insert into users (name,postcode) values (#{name},#{postcode})")
  @Options(useGeneratedKeys = true)
  void save(UserDataEntity userDataEntity);
  //public int insertOne( MUser user);

  @Update("update users set name=#{name}, postcode=#{postcode} where id=#{id}")
  void update(UserDataEntity userDataEntity);

  @Delete("delete from users where id=#{id}")
  void delete(Integer id);


}

