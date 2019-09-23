package com.trabricks.data.jpa.configs;

import com.trabricks.data.jpa.support.EnversAndDatatablesRepositoryFactoryBean;
import lombok.extern.slf4j.Slf4j;
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
public class DataJpaConfig {

}