package com.trabricks.security.configs;

import com.trabricks.security.properties.WebSecurityProperties;
import com.trabricks.security.support.RestAuthenticationEntryPoint;
import com.trabricks.security.support.RestAuthenticationFailureHandler;
import com.trabricks.security.support.RestAuthenticationSuccessHandler;
import com.trabricks.security.support.RestLogoutSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author eomjeongjae
 * @since 2019-09-24
 */
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(WebSecurityProperties.class)
public abstract class AbstractWebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private WebSecurityProperties webSecurityProperties;

  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  @Bean
  @ConditionalOnMissingBean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().mvcMatchers("/docs/index.html", "/vendors/**");
    web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    log.info("webSecurityProperties: {}", webSecurityProperties);
    log.info("webSecurityProperties.rest: {}", webSecurityProperties.getRest());

    http
        .formLogin()
        .loginPage("/login")
        .permitAll()
        .and()
        .logout()
        .logoutUrl("/logout");

    if (webSecurityProperties.getRest().isEnabled()) {
      http
          .exceptionHandling()
          .authenticationEntryPoint(restAuthenticationEntryPoint())
          .and()
          .formLogin()
          .successHandler(restAuthenticationSuccessHandler())
          .failureHandler(restAuthenticationFailureHandler())
          .and()
          .logout()
          .logoutSuccessHandler(restLogoutSuccessHandler());
    }

    this.configureHttpSecurity(http);
  }

  protected void configureHttpSecurity(HttpSecurity http) throws Exception {
    // nothing
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
