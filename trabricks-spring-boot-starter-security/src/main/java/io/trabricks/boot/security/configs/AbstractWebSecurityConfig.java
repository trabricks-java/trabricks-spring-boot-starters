package io.trabricks.boot.security.configs;

import io.trabricks.boot.security.properties.WebSecurityProperties;
import io.trabricks.boot.security.support.RestAuthenticationEntryPoint;
import io.trabricks.boot.security.support.RestAuthenticationFailureHandler;
import io.trabricks.boot.security.support.RestAuthenticationSuccessHandler;
import io.trabricks.boot.security.support.RestLogoutSuccessHandler;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author eomjeongjae
 * @since 2019-09-24
 */
@EnableConfigurationProperties(WebSecurityProperties.class)
public abstract class AbstractWebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private WebSecurityProperties webSecurityProperties;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

  @Autowired
  private RestAuthenticationFailureHandler restAuthenticationFailureHandler;

  @Autowired
  private RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;

  @Autowired
  private RestLogoutSuccessHandler restLogoutSuccessHandler;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder);
  }

  @Bean
  @ConditionalOnMissingBean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  public void configure(WebSecurity web) {
    web
        .ignoring()
        .mvcMatchers(
            "/docs/index.html",
            "/vendors/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/fonts/**"
        )
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .formLogin()
        .loginPage("/login")
        .permitAll()
        .and()
        .logout()
        .logoutUrl("/logout")
        .and()
        .csrf()
        .requireCsrfProtectionMatcher(new CsrfSecurityRequestMatcher())
    ;

    if (webSecurityProperties.getRest().isEnabled()) {
      http
          .exceptionHandling()
          .authenticationEntryPoint(restAuthenticationEntryPoint)
          .and()
          .formLogin()
          .successHandler(restAuthenticationSuccessHandler)
          .failureHandler(restAuthenticationFailureHandler)
          .and()
          .logout()
          .logoutSuccessHandler(restLogoutSuccessHandler);
    }
  }

  protected class CsrfSecurityRequestMatcher implements RequestMatcher {

    private Pattern allowedMethods;
    private RegexRequestMatcher unprotectedMatcher;

    public CsrfSecurityRequestMatcher() {
      this(".*(/files|/lambda|/dataTables.json).*");
    }

    public CsrfSecurityRequestMatcher(String unprotectedPattern) {
      this.allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
      this.unprotectedMatcher = new RegexRequestMatcher(unprotectedPattern, null);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
      if (allowedMethods.matcher(request.getMethod()).matches()) {
        return false;
      }

      return !unprotectedMatcher.matches(request);
    }
  }

}
