package io.trabricks.boot.web.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hectorlopezfernandez.pebble.springsecurity.SpringSecurityExtension;
import io.trabricks.boot.commons.configs.CommonConfig;
import io.trabricks.boot.web.common.CommonRestControllerAdvice;
import io.trabricks.boot.web.interceptors.WebInterceptor;
import io.trabricks.boot.web.notice.properties.FirebaseProperties;
import io.trabricks.boot.web.notice.service.DefaultFirebaseMessageServiceImpl;
import io.trabricks.boot.web.notice.service.FirebaseMessageService;
import io.trabricks.boot.web.pebble.PebbleViewExtension;
import io.trabricks.boot.web.storage.properties.StorageProperties;
import io.trabricks.boot.web.storage.service.FileSystemStorageService;
import io.trabricks.boot.web.storage.service.StorageService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestTemplate;
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
@EnableConfigurationProperties({StorageProperties.class, FirebaseProperties.class})
@AutoConfigureAfter(value = {WebMvcAutoConfiguration.class, CommonConfig.class})
public class WebMvcConfig implements WebMvcConfigurer {

  private final ModelMapper modelMapper;
  private final Environment environment;
  private final ObjectMapper objectMapper;
  private final StorageProperties storageProperties;
  private final FirebaseProperties firebaseProperties;

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

  @Bean
  @ConditionalOnMissingBean
  public PebbleViewExtension pebbleViewExtension() {
    return new PebbleViewExtension(messageSourceAccessor(), objectMapper);
  }

  @Bean
  @ConditionalOnMissingBean
  public SpringSecurityExtension securityExtension() {
    return new SpringSecurityExtension();
  }

  @Bean
  @ConditionalOnMissingBean
  public StorageService storageService() {
    return new FileSystemStorageService(storageProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(prefix = "firebase", name = {"private-key-path", "database-url"})
  public FirebaseMessageService firebaseMessageService() {
    return new DefaultFirebaseMessageServiceImpl(firebaseProperties, restTemplate(), objectMapper);
  }

  @Bean
  @ConditionalOnMissingBean
  public RestTemplateBuilder restTemplateBuilder() {
    return new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(1))
        .setReadTimeout(Duration.ofSeconds(1));
  }

  @Bean
  @ConditionalOnMissingBean
  public RestTemplate restTemplate() {
    return restTemplateBuilder().build();
  }

}
