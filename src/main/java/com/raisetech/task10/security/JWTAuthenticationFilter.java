package com.raisetech.task10.security;

import static com.raisetech.task10.security.SecurityConstants.EXPIRATION_TIME;
import static com.raisetech.task10.security.SecurityConstants.HEADER_STRING;
import static com.raisetech.task10.security.SecurityConstants.LOGIN_ID;
import static com.raisetech.task10.security.SecurityConstants.LOGIN_URL;
import static com.raisetech.task10.security.SecurityConstants.PASSWORD;
import static com.raisetech.task10.security.SecurityConstants.SECRET;
import static com.raisetech.task10.security.SecurityConstants.TOKEN_PREFIX;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raisetech.task10.form.LoginForm;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


public class JWTAuthenticationFilter extends
    UsernamePasswordAuthenticationFilter {
  // UsernamePasswordAuthenticationFilterをそのまま利用することもできるが、
  // レスポンスをカスタマイズするため、これを継承してoverrideする
  private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

  private AuthenticationManager authenticationManager;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.authenticationManager = authenticationManager;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    // ログイン用のpathを変更する
    setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(LOGIN_URL, "POST"));

    // ログイン用のID/PWのパラメータ名を変更する
    setUsernameParameter(LOGIN_ID);
    setPasswordParameter(PASSWORD);

  }

  // 認証の処理
  @Override
  public Authentication attemptAuthentication(HttpServletRequest req,
                                              HttpServletResponse res) throws
      AuthenticationException {
    try {
      // requestパラメータからユーザ情報を読み取る
      LoginForm loginForm = new ObjectMapper().readValue(req.getInputStream(), LoginForm.class);
      // UsernamePasswordAuthenticationTokenを生成してauthenticationManagerに渡す。
      // UsernamePasswordAuthenticationFilterのデフォルト実装だとauthoritiesが設定されない
      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginForm.getLoginId(),
              loginForm.getPass(),
              new ArrayList<>())
      );
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }


  // 認証に成功した場合の処理
  @Override
  protected void successfulAuthentication(HttpServletRequest req,
                                          HttpServletResponse res,
                                          FilterChain chain,
                                          Authentication auth) throws IOException,
      ServletException {
    // loginIdからtokenを設定してヘッダにセットする
    String token = Jwts.builder() //JWTトークンを生成。
        .setSubject(((User)auth.getPrincipal()).getUsername()) // usernameだけを設定する
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
        .compact();
    res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);  //レスポンスに書き込み

    // ここでレスポンスを組み立てると個別のパラメータを返せるがFilterの責務の範囲内で実施しなければならない
    // auth.getPrincipal()で取得できるUserDetailsは自分で作ったEntityクラスにもできるのでカスタム属性は追加可能
  }
}
