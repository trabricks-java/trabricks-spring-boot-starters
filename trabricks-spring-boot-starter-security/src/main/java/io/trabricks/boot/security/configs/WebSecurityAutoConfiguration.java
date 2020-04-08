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

/**
 * The type Web security auto configuration.
 */
@Configuration
public class WebSecurityAutoConfiguration {

  /**
   * Password encoder password encoder.
   *
   * @return the password encoder
   */
  @Bean
  @ConditionalOnMissingBean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  /**
   * Rest authentication entry point rest authentication entry point.
   *
   * @return the rest authentication entry point
   */
  @Bean
  @ConditionalOnMissingBean
  public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
    return new RestAuthenticationEntryPoint();
  }

  /**
   * Rest authentication failure handler rest authentication failure handler.
   *
   * @return the rest authentication failure handler
   */
  @Bean
  @ConditionalOnMissingBean
  public RestAuthenticationFailureHandler restAuthenticationFailureHandler() {
    return new RestAuthenticationFailureHandler();
  }

  /**
   * Rest authentication success handler rest authentication success handler.
   *
   * @return the rest authentication success handler
   */
  @Bean
  @ConditionalOnMissingBean
  public RestAuthenticationSuccessHandler restAuthenticationSuccessHandler() {
    return new RestAuthenticationSuccessHandler();
  }

  /**
   * Rest logout success handler rest logout success handler.
   *
   * @return the rest logout success handler
   */
  @Bean
  @ConditionalOnMissingBean
  public RestLogoutSuccessHandler restLogoutSuccessHandler() {
    return new RestLogoutSuccessHandler();
  }

}
