package com.trabricks.data.jpa.configs;

import com.trabricks.commons.configs.CommonConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author eomjeongjae
 * @since 2019-09-20
 */
@Slf4j
@Configuration
@EnableJpaAuditing
@AutoConfigureAfter(CommonConfig.class)
public class DataJpaConfig {

}