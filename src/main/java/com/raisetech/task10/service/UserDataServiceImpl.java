package com.raisetech.task10.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raisetech.task10.advice.BadRequestException;
import com.raisetech.task10.advice.NotFoundException;
import com.raisetech.task10.entity.UserDataEntity;
import com.raisetech.task10.mapper.UserDataMapper;
import java.util.List;
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

  @Transactional
  @Override
  //全件データ取得
  public List<UserDataEntity> findAllUserData() {
    return userDataMapper.findAllUserData();
  }

  @Transactional
  @Override
  //指定IDデータ取得。ID存在しない場合はNotFound例外を投げる。
  public UserDataEntity findOneUserData(Integer id) {
    return this.userDataMapper.findOneUserData(id)
        .orElseThrow(
            () -> new NotFoundException("idが " + id + " のデータは存在しません。"));
  }

  @Transactional
  @Override
  //データ新規作成
  public void saveUserData(UserDataEntity userDataEntity) {
    userDataMapper.saveUserData(userDataEntity);
  }

  @Transactional
  @Override
  //データ更新
  public void updateUserData(UserDataEntity userDataEntity) {
    userDataMapper.updateUserData(userDataEntity);
  }

  @Transactional
  @Override
  //指定IDデータ削除
  public void deleteUserData(Integer id) {
    userDataMapper.deleteUserData(id);
  }


  //Yahooデベロッパーネットワークにて郵便番号から住所を取得する
  private static final String URL =
      "https://map.yahooapis.jp/search/zip/V1/zipCodeSearch?query={postcode}&appid=dj00aiZpPXBHZkFNY2VmVkk3aSZzPWNvbnN1bWVyc2VjcmV0Jng9MTQ-&output=json";

  @Override
  public String fetchAddress(String postCode) {
    //郵便番号を引数で受け取り、外部apiから住所を取得しreturnする
    String postCodeJson =
        restTemplate.getForObject(URL, String.class, postCode);
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      JsonNode jsonNode = objectMapper.readTree(postCodeJson);
      String count = jsonNode.get("ResultInfo").get("Count").toString();
      if (count.equals("0")) {
        throw new BadRequestException("郵便番号" + postCode + "は存在しません。");
      }
      String address =
          jsonNode.get("Feature").get(0).get("Property").get("Address")
              .toString().replaceAll("\"", "");
      return address;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
