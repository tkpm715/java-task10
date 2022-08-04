package com.raisetech.task10;

import static com.raisetech.task10.security.SecurityConstants.LOGIN_URL;
import static com.raisetech.task10.security.SecurityConstants.SIGNUP_URL;

import com.raisetech.task10.security.JWTAuthenticationFilter;
import com.raisetech.task10.security.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;

  //protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  protected void configure(HttpSecurity http) throws Exception {
    http
        .cors()
        .and().authorizeRequests()
        .antMatchers("/public", SIGNUP_URL, LOGIN_URL).permitAll() //この３つのページは認証不要
        .anyRequest().authenticated() //それ以外のページは認証が必要
        .and().logout()
        .and().csrf().disable()//CSRFを無効化
        //addFilterでログイン時に利用するFilterを指定します。 FormログインのFilterを置き換える
        .addFilter(new JWTAuthenticationFilter(authenticationManager(), bCryptPasswordEncoder()))
        .addFilter(new JWTAuthorizationFilter(authenticationManager()))
        //認証情報はJWTで毎回やりとりを行うため、セッションを保持させない。そのための設定
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //return http.build();
  }


  //認証するユーザー情報をデータベースからロードする
//その際、パスワードはBCryptで暗号化した値を利用する
  @Autowired
  public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(bCryptPasswordEncoder());
        //.passwordEncoder(NoOpPasswordEncoder.getInstance());
  }

   //パスワードをBCryptで暗号化するクラス
  @Bean
 public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
