package io.trabricks.boot.data.jpa.configs;

import io.trabricks.boot.commons.configs.CommonConfig;
import io.trabricks.boot.data.jpa.support.DataTablesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author eomjeongjae
 * @since 2019-09-20
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableJpaAuditing
@AutoConfigureAfter(CommonConfig.class)
public class DataJpaConfig {

  private final ModelMapper modelMapper;

  @Bean
  @ConditionalOnMissingBean
  public DataTablesService dataTablesService() {
    return new DataTablesService(modelMapper);
  }
}
