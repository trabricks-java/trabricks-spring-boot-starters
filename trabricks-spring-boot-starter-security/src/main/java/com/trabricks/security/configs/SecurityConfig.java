package com.trabricks.security.configs;

import com.trabricks.security.support.RestAuthenticationEntryPoint;
import com.trabricks.security.support.RestAuthenticationFailureHandler;
import com.trabricks.security.support.RestAuthenticationSuccessHandler;
import com.trabricks.security.support.RestLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author eomjeongjae
 * @since 2019-09-24
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@AutoConfigureAfter(SecurityAutoConfiguration.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {



  @Override
  @Bean
  @ConditionalOnMissingBean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

//  @Override
//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    auth.userDetailsService(accountService).passwordEncoder(passwordEncoder());
//  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().mvcMatchers("/docs/index.html");
    web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Bean
  @ConditionalOnMissingBean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  @ConditionalOnMissingBean
  public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
    return new RestAuthenticationEntryPoint();
  }

  @Bean
  @ConditionalOnMissingBean
  public RestAuthenticationFailureHandler restAuthenticationFailureHandler() {
    return new RestAuthenticationFailureHandler();
  }

  @Bean
  @ConditionalOnMissingBean
  public RestAuthenticationSuccessHandler restAuthenticationSuccessHandler() {
    return new RestAuthenticationSuccessHandler();
  }

  @Bean
  @ConditionalOnMissingBean
  public RestLogoutSuccessHandler restLogoutSuccessHandler() {
    return new RestLogoutSuccessHandler();
  }

}
