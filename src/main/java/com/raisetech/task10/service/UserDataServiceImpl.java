package com.raisetech.task10.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raisetech.task10.advice.BadRequestException;
import com.raisetech.task10.advice.NotFoundException;
import com.raisetech.task10.controller.UserDataResponse;
import com.raisetech.task10.entity.UserDataEntity;
import com.raisetech.task10.form.UserDataForm;
import com.raisetech.task10.mapper.RefillMapper;
import com.raisetech.task10.mapper.UserDataMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class UserDataServiceImpl implements UserDataService {
  private final UserDataMapper userDataMapper;
  private final RestTemplate restTemplate;

  public UserDataServiceImpl(UserDataMapper userDataMapper,
                             RestTemplateBuilder restTemplateBuilder) {
    this.userDataMapper = userDataMapper;
    this.restTemplate = restTemplateBuilder.build();
  }

  @Override
  //全件データ取得
  public List<UserDataResponse> findAllUserData() {
    return userDataMapper.findAllUserData().stream()
        .map(n -> new UserDataResponse(n.getId(), n.getName(), n.getPostcode(),
            fetchAddress(n.getPostcode()))).collect(Collectors.toList());
  }

  @Override
  //指定IDデータ取得。ID存在しない場合はNotFound例外を投げる。
  public UserDataResponse findOneUserData(int id) {
    //パスパラメータ指定のIDに該当するデータを取得
    UserDataEntity userDataEntity =
        this.userDataMapper.findOneUserData(id).orElseThrow(() ->
            new NotFoundException("idが " + id + " のデータは存在しません。"));

    RefillMapper refillMapper = Mappers.getMapper(RefillMapper.class);
    UserDataResponse userDataResponse =
        refillMapper.useDataEntityToUserDataResponse(userDataEntity);
    //郵便番号を引数に渡し返ってきた住所データをResponseクラスに詰める
    userDataResponse.setAddress(
        this.fetchAddress(userDataResponse.getPostcode()));
    return userDataResponse;
  }

  @Transactional
  @Override
  //データ新規作成
  public void saveUserData(UserDataForm userDataForm) {
    //存在しない郵便番号であった場合は登録できないよう例外を投げる
    if (fetchAddress(userDataForm.getPostcode()).equals("")) {
      throw new BadRequestException(
          "郵便番号" + userDataForm.getPostcode() + "は存在しません。データ登録に失敗しました。");
    }
    RefillMapper refillMapper = Mappers.getMapper(RefillMapper.class);
    UserDataEntity userDataEntity =
        refillMapper.useDataFormToUserDataEntity(userDataForm);
    userDataMapper.saveUserData(userDataEntity);
  }

  @Transactional
  @Override
  //データ更新
  public void updateUserData(UserDataForm userDataForm, int id) {
    //パスパラメータ指定のIDが存在するかチェック
    UserDataResponse dataExistenceCheck = this.findOneUserData(id);
    //ユーザIDはパスパラメータ、名前と郵便番号はbodyで受け取る。
    //IDをformに詰める
    userDataForm.setId(id);
    RefillMapper refillMapper = Mappers.getMapper(RefillMapper.class);
    UserDataEntity userDataEntity =
        refillMapper.useDataFormToUserDataEntity(userDataForm);
    userDataMapper.updateUserData(userDataEntity);
  }

  @Transactional
  @Override
  //指定IDデータ削除
  public void deleteUserData(Integer id) {
    //パスパラメータ指定のIDが存在するかチェック
    UserDataResponse dataExistenceCheck = this.findOneUserData(id);
    userDataMapper.deleteUserData(id);
  }

  @Value("${YAHOO_URL}")
  private String yahooURL;

  @Override
  public String fetchAddress(String postCode) {
    //郵便番号を引数で受け取り、外部apiから住所を取得しreturnする
    String postCodeJson =
        restTemplate.getForObject(yahooURL, String.class, postCode);
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      JsonNode jsonNode = objectMapper.readTree(postCodeJson);
      String count = jsonNode.get("ResultInfo").get("Count").toString();

      //郵便番号が存在しない場合は空を返す（廃止になってしまった郵便番号への対処）
      String userAddress = "";
      if (!(count.equals("0"))) {
        userAddress =
            jsonNode.get("Feature").get(0).get("Property").get("Address")
                .toString().replaceAll("\"", "");
      }
      return userAddress;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
