package com.raisetech.task10.controller;


import com.raisetech.task10.advice.BadRequestException;
import com.raisetech.task10.entity.UserDataEntity;
import com.raisetech.task10.form.UserDataForm;
import com.raisetech.task10.mapper.RefillMapper;
import com.raisetech.task10.service.UserDataService;
import java.util.List;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class UserDataController {
  private final UserDataService userDataService;

  public UserDataController(UserDataService userDataService) {
    this.userDataService = userDataService;
  }

  @GetMapping("/users/{id}")
  public UserDataResponse displayUserData(@PathVariable("id") int id) {
    //パスパラメータ指定のIDに該当するデータを取得
    UserDataEntity userDataEntity = userDataService.findOneUserData(id);
    RefillMapper refillMapper = Mappers.getMapper(RefillMapper.class);
    UserDataResponse userDataResponse =
        refillMapper.useDataEntityToUserDataResponse(userDataEntity);
    //郵便番号を引数に渡し返ってきた住所データをResponseクラスに詰める
    userDataResponse.setAddress(
        userDataService.fetchAddress(userDataResponse.getPostcode()));
    //指定IDに対するユーザ情報に加え外部apiから取得した住所を表示
    return userDataResponse;
  }

  @GetMapping("/users/")
  public List<UserDataEntity> displayAllUserDat() {
    return userDataService.findAllUserData();
  }

  @PostMapping("/users/")
  public ResponseEntity<String> createUserDate(
      @RequestBody @Validated UserDataForm userDataForm,
      Errors errors) {
    if (errors.hasErrors()) {
      //バリデーションに引っかかった場合は400BadRequestを表示
      throw new BadRequestException(errors.getFieldError().getDefaultMessage());
    }

    RefillMapper refillMapper = Mappers.getMapper(RefillMapper.class);
    UserDataEntity userDataEntity =
        refillMapper.useDataFormToUserDataEntity(userDataForm);
    userDataService.saveUserData(userDataEntity);
    return ResponseEntity.ok("データの登録が完了しました");
  }

  @PatchMapping("/users/{id}")
  public ResponseEntity<String> changeUserData(@PathVariable("id") int id,
                                               @RequestBody @Validated
                                               UserDataForm userDataForm,
                                               Errors errors) {
    if (errors.hasErrors()) {
      //バリデーションに引っかかった場合は400BadRequestを表示
      throw new BadRequestException(errors.getFieldError().getDefaultMessage());
    }
    //パスパラメータ指定のIDが存在するかチェック
    UserDataEntity dataExistenceCheck = userDataService.findOneUserData(id);
    //ユーザIDはパスパラメータ、名前と郵便番号はbodyで受け取る。
    //IDをformに詰める
    userDataForm.setId(id);
    RefillMapper refillMapper = Mappers.getMapper(RefillMapper.class);
    UserDataEntity userDataEntity =
        refillMapper.useDataFormToUserDataEntity(userDataForm);
    userDataService.updateUserData(userDataEntity);
    return ResponseEntity.ok("データを変更しました");
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<String> deleteUserData(@PathVariable("id") int id) {
    //パスパラメータ指定のIDが存在するかチェック
    UserDataEntity dataExistenceCheck = userDataService.findOneUserData(id);
    userDataService.deleteUserData(id);
    return ResponseEntity.ok("データを削除しました");
  }

}
