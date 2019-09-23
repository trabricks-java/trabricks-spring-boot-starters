package com.trabricks.web.configs;

import com.trabricks.commons.configs.CommonConfig;
import com.trabricks.web.common.CommonRestControllerAdvice;
import com.trabricks.web.interceptors.WebInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * @author eomjeongjae
 * @since 2019-07-22
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@AutoConfigureAfter(value={WebMvcAutoConfiguration.class, CommonConfig.class})
public class WebMvcConfig implements WebMvcConfigurer {

  private final ModelMapper modelMapper;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(webInterceptor());
    registry.addInterceptor(localeChangeInterceptor());
  }

  @Bean
  @ConditionalOnMissingBean(name = "webInterceptor")
  public HandlerInterceptor webInterceptor() {
    return new WebInterceptor();
  }

  @Bean
  @ConditionalOnMissingBean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("lang");
    return localeChangeInterceptor;
  }

  @Bean
  @ConditionalOnMissingBean
  public LocaleResolver localeResolver() {
    CookieLocaleResolver localeResolver = new CookieLocaleResolver();
    localeResolver.setCookieName("i18n");
    localeResolver.setCookieMaxAge(-1);
    localeResolver.setCookiePath("/");
    return localeResolver;
  }

  @Bean
  @ConditionalOnMissingBean
  public MessageSourceAccessor messageSourceAccessor() {
    return new MessageSourceAccessor(messageSource());
  }

  @Bean
  @ConditionalOnMissingBean
  public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:/i18n/messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Bean
  @ConditionalOnMissingBean(annotation = RestControllerAdvice.class)
  public CommonRestControllerAdvice commonRestControllerAdvice() {
    return new CommonRestControllerAdvice(modelMapper);
  }

}