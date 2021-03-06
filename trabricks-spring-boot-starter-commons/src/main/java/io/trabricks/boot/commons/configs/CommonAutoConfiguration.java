package io.trabricks.boot.commons.configs;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Common auto configuration.
 *
 * @author eomjeongjae
 * @since 2019 -09-23
 */
@Configuration
@ConditionalOnClass(ModelMapper.class)
public class CommonAutoConfiguration {

  /**
   * Model mapper model mapper.
   *
   * @return the model mapper
   */
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
