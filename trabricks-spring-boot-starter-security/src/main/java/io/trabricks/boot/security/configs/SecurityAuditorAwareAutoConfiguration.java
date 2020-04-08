package io.trabricks.boot.security.configs;

import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * The type Security auditor aware auto configuration.
 *
 * @author eomjeongjae
 * @since 2019 -07-18
 */
@Configuration
public class SecurityAuditorAwareAutoConfiguration {

  /**
   * Auditor provider auditor aware.
   *
   * @return the auditor aware
   */
  @Bean
  @ConditionalOnMissingBean
  public AuditorAware<String> auditorProvider() {
    return new SecurityAuditorAware();
  }

  /**
   * The type Security auditor aware.
   */
  public static class SecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
      return Optional.ofNullable(SecurityContextHolder.getContext())
          .map(SecurityContext::getAuthentication)
          .filter(Authentication::isAuthenticated)
          .filter(authentication -> !AnonymousAuthenticationToken.class
              .isAssignableFrom(authentication.getClass()))
          .map(authentication -> {
            if (OAuth2Authentication.class
                .isAssignableFrom(authentication.getClass())) {
              return authentication.getPrincipal().toString();
            }
            return ((UserDetails) authentication.getPrincipal()).getUsername();
          });
    }
  }

}
