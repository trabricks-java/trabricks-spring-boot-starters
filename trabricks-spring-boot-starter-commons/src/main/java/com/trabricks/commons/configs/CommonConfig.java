package com.trabricks.commons.configs;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author eomjeongjae
 * @since 2019-09-23
 */
@Configuration
public class CommonConfig {

  @Bean
  @ConditionalOnMissingBean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
        .setPropertyCondition(Conditions.isNotNull())
        .setMatchingStrategy(MatchingStrategies.STRICT)
        .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
        .setFieldMatchingEnabled(true);
    return modelMapper;
  }

}
