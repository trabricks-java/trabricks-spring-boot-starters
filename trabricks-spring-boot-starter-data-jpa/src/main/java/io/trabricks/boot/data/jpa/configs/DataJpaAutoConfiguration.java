package io.trabricks.boot.data.jpa.configs;

import io.trabricks.boot.data.jpa.support.DataTablesService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * The type Data jpa auto configuration.
 *
 * @author eomjeongjae
 * @since 2019 -09-20
 */
@RequiredArgsConstructor
@EnableJpaAuditing
@Configuration
public class DataJpaAutoConfiguration {

  private final ModelMapper modelMapper;

  /**
   * Data tables service data tables service.
   *
   * @return the data tables service
   */
  @Bean
  @ConditionalOnMissingBean
  public DataTablesService dataTablesService() {
    return new DataTablesService(modelMapper);
  }
}
