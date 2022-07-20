package com.raisetech.task10.controller;

import static com.raisetech.task10.security.SecurityConstants.SIGNUP_URL;
import static com.raisetech.task10.security.SecurityConstants.LOGIN_ID;

import com.raisetech.task10.advice.BadRequestException;
import com.raisetech.task10.entity.UserDataEntity;
import com.raisetech.task10.form.UserDataForm;
import com.raisetech.task10.mapper.RefillMapper;
import com.raisetech.task10.service.UserDataService;
import java.util.List;
import javax.validation.Valid;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.raisetech.task10.form.LoginForm;
import com.raisetech.task10.security.JWTAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;



@RestController
@Validated
public class UserDataController {

  private final UserDataService userDataService;
  private static final Logger LOGGER = LoggerFactory.getLogger(UserDataController.class);

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  public UserDataController(UserDataService userDataService) {
    this.userDataService = userDataService;
  }

//    if (manageUserDataService.findMatch(year).stream().map(ManageUserDataResponse::new).toList()
//        .isEmpty() ) {
//      throw new MyException("入力に誤りがあります。年の値として2010～2019を入力してください。");
//    }
//    return manageUserDataService.findMatch(year).stream().map(ManageUserDataResponse::new)
//        .toList();

  @GetMapping(value = "/public")
  public String publicApi() {
    return "This page is public";
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
    UserDataEntity userDataEntity = userDataService.findOne(id);

    RefillMapper refillMapper = Mappers.getMapper(RefillMapper.class);
    UserDataResponse userDataResponse =
        refillMapper.useDataEntityToUserDataResponse(userDataEntity);

    userDataResponse.setAddress(
        userDataService.fetchAddress(userDataResponse.getPostcode()));

    return userDataResponse;
  }

  @GetMapping("/users/")
  public List<UserDataEntity> displayAllUserDat() {
    //return userDataService.findAll().stream().map(UserDataResponse::new).toList();
    return userDataService.findAll();
  }

  @PostMapping("/users/")
  public String createUserDate(@RequestBody @Valid UserDataForm userDataForm,
                               BindingResult result) {
    if (result.hasErrors()) {
      throw new BadRequestException(result.getFieldError().getDefaultMessage());
    }

    RefillMapper refillMapper = Mappers.getMapper(RefillMapper.class);
    UserDataEntity userDataEntity =
        refillMapper.useDataFormToUserDataEntity(userDataForm);
    userDataService.save(userDataEntity);
    return "登録が完了しました";
  }

  @PatchMapping("/users/{id}")
  public String changeUserData(@PathVariable("id") int id,
                               @RequestBody @Valid UserDataForm userDataForm,
                               BindingResult result) {
    if (result.hasErrors()) {
      throw new BadRequestException(result.getFieldError().getDefaultMessage());
    }
    userDataForm.setId(id);
    RefillMapper refillMapper = Mappers.getMapper(RefillMapper.class);
    UserDataEntity userDataEntity =
        refillMapper.useDataFormToUserDataEntity(userDataForm);
    userDataService.update(userDataEntity);
    return "データを変更しました";
  }

  @DeleteMapping("/users/{id}")
  public String deleteUserData(@PathVariable("id") int id) {
    userDataService.delete(id);
    return "データを削除しました";
  }

}
