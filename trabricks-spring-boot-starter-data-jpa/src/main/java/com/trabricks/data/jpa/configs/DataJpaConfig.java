package com.trabricks.data.jpa.configs;

import com.trabricks.commons.configs.CommonConfig;
import com.trabricks.data.jpa.support.EnversAndDatatablesRepositoryFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author eomjeongjae
 * @since 2019-09-20
 */
@Slf4j
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversAndDatatablesRepositoryFactoryBean.class)
@AutoConfigureAfter(CommonConfig.class)
public class DataJpaConfig {

}