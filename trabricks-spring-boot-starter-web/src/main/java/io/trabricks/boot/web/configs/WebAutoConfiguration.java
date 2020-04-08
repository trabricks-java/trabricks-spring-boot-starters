package io.trabricks.boot.web.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.trabricks.boot.web.common.CommonRestControllerAdvice;
import io.trabricks.boot.web.interceptors.WebLogInterceptor;
import io.trabricks.boot.web.notice.properties.FirebaseProperties;
import io.trabricks.boot.web.notice.service.DefaultFirebaseMessageServiceImpl;
import io.trabricks.boot.web.notice.service.FirebaseMessageService;
import io.trabricks.boot.web.pebble.PebbleViewExtension;
import io.trabricks.boot.web.storage.properties.StorageProperties;
import io.trabricks.boot.web.storage.service.FileSystemStorageService;
import io.trabricks.boot.web.storage.service.StorageService;
import io.trabricks.boot.web.views.excel.components.ExcelReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * The type Web auto configuration.
 *
 * @author eomjeongjae
 * @since 2019 -07-22
 */
@Slf4j
@RequiredArgsConstructor
@EnableAsync
@Configuration
@ConditionalOnClass(RestTemplate.class)
@EnableConfigurationProperties({StorageProperties.class, FirebaseProperties.class})
public class WebAutoConfiguration implements WebMvcConfigurer {

  private final ModelMapper modelMapper;
  private final ObjectMapper objectMapper;
  private final StorageProperties storageProperties;
  private final FirebaseProperties firebaseProperties;
  private final RestTemplateBuilder restTemplateBuilder;
  private final MessageSource messageSource;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new WebLogInterceptor())
        .excludePathPatterns(
            "/css/**",
            "/js/**",
            "/fonts/**",
            "/img/**",
            "/images/**",
            "/webfonts/**",
            "/i18n/**",
            "/favicon.ico",
            "/assets/**",
            "/static-bundle/**"
        );
    registry.addInterceptor(localeChangeInterceptor());
  }

  /**
   * Locale change interceptor locale change interceptor.
   *
   * @return the locale change interceptor
   */
  @Bean
  @ConditionalOnMissingBean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("lang");
    return localeChangeInterceptor;
  }

  /**
   * Locale resolver locale resolver.
   *
   * @return the locale resolver
   */
  @Bean
  @ConditionalOnMissingBean
  public LocaleResolver localeResolver() {
    CookieLocaleResolver localeResolver = new CookieLocaleResolver();
    localeResolver.setCookieName("i18n");
    localeResolver.setCookieMaxAge(-1);
    localeResolver.setCookiePath("/");
    return localeResolver;
  }

  /**
   * Message source accessor message source accessor.
   *
   * @return the message source accessor
   */
  @Bean
  @ConditionalOnMissingBean
  public MessageSourceAccessor messageSourceAccessor() {
    return new MessageSourceAccessor(messageSource);
  }

  /*
  // spring boot autoconfig 활용
  @Bean
  @ConditionalOnMissingBean
  public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath*:/i18n/messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }*/

  /**
   * Common rest controller advice common rest controller advice.
   *
   * @return the common rest controller advice
   */
  @Bean
  @ConditionalOnMissingBean(annotation = RestControllerAdvice.class)
  public CommonRestControllerAdvice commonRestControllerAdvice() {
    return new CommonRestControllerAdvice(messageSourceAccessor(), modelMapper);
  }

  /**
   * Pebble view extension pebble view extension.
   *
   * @return the pebble view extension
   */
  @Bean
  @ConditionalOnMissingBean
  public PebbleViewExtension pebbleViewExtension() {
    return new PebbleViewExtension(messageSourceAccessor(), objectMapper);
  }

  /**
   * Storage service storage service.
   *
   * @return the storage service
   */
  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(prefix = "storage", name = "location")
  public StorageService storageService() {
    return new FileSystemStorageService(storageProperties);
  }

  /**
   * Firebase message service firebase message service.
   *
   * @return the firebase message service
   */
  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(prefix = "firebase", name = {"private-key-path", "database-url"})
  public FirebaseMessageService firebaseMessageService() {
    return new DefaultFirebaseMessageServiceImpl(firebaseProperties, objectMapper);
  }

  /**
   * Rest template rest template.
   *
   * @return the rest template
   */
  @Bean
  @ConditionalOnMissingBean
  public RestTemplate restTemplate() {
    return restTemplateBuilder.build();
  }

  /**
   * Excel reader excel reader.
   *
   * @return the excel reader
   */
  @Bean
  @ConditionalOnMissingBean
  public ExcelReader excelReader() {
    return new ExcelReader();
  }

}
