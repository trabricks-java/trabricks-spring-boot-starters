package io.trabricks.boot.security.configs;

import io.trabricks.boot.security.support.RestAuthenticationEntryPoint;
import io.trabricks.boot.security.support.RestAuthenticationFailureHandler;
import io.trabricks.boot.security.support.RestAuthenticationSuccessHandler;
import io.trabricks.boot.security.support.RestLogoutSuccessHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityAutoConfiguration {

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
