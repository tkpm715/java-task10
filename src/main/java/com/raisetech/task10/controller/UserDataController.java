package com.raisetech.task10.controller;


import static com.raisetech.task10.security.SecurityConstants.SIGNUP_URL;

import com.raisetech.task10.advice.BadRequestException;
import com.raisetech.task10.form.LoginForm;
import com.raisetech.task10.form.UserDataForm;
import com.raisetech.task10.service.UserDataService;
import java.util.List;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
  private static final Logger LOGGER = LoggerFactory.getLogger(UserDataController.class);

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @GetMapping(value = "/public")
  public String publicApi() {
    return "this is public";
  }

  @GetMapping(value = "/private")
  public String privateApi() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // JWTAuthenticationFilter#successfulAuthenticationで設定したusernameを取り出す
    String username = (String) (authentication.getPrincipal());

    return "this is private for " + username;
  }

  @PostMapping(value = SIGNUP_URL)
  public void signup(@Valid @RequestBody LoginForm user) {

    // passwordを暗号化する
    user.encrypt(bCryptPasswordEncoder);

    // DBに保存する処理を本来は書く
    LOGGER.info("signup :" + user.toString());
  }
  @GetMapping("/users/{id}")
  public UserDataResponse displayUserData(@PathVariable("id") int id) {
    //パスパラメータ指定IDに対するユーザ情報に加え外部apiから取得した住所を表示
    return userDataService.findOneUserData(id);
  }

  @GetMapping("/users/")
  public List<UserDataResponse> displayAllUserDat() {
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
        userDataService.saveUserData(userDataForm);
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
    userDataService.updateUserData(userDataForm,id);
    return ResponseEntity.ok("データを変更しました");
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<String> deleteUserData(@PathVariable("id") int id) {
    userDataService.deleteUserData(id);
    return ResponseEntity.ok("データを削除しました");
  }

}
