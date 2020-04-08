package io.trabricks.boot.security.configs;

import io.trabricks.boot.security.properties.WebSecurityProperties;
import io.trabricks.boot.security.properties.WebSecurityProperties.Oauth;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * The type Authorization server auto configuration.
 *
 * @author eomjeongjae
 * @since 29 /09/2019
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "trabricks.security", name = "oauth.enabled", havingValue = "true")
@ConditionalOnBean(AuthenticationManager.class)
@EnableAuthorizationServer
@EnableConfigurationProperties(WebSecurityProperties.class)
public class AuthorizationServerAutoConfiguration extends AuthorizationServerConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final WebSecurityProperties webSecurityProperties;
  private final UserDetailsService userDetailsService;

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints
        .tokenStore(tokenStore())
        .accessTokenConverter(accessTokenConverter())
        .userDetailsService(userDetailsService)
        .authenticationManager(authenticationManager)
    ;
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security
        .tokenKeyAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
        .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
        .allowFormAuthenticationForClients();
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    Oauth oauthProperties = webSecurityProperties.getOauth();
    clients.inMemory()
        .withClient(oauthProperties.getClientId())
        .authorizedGrantTypes("password", "refresh_token", "client_credentials")
        .authorities("ROLE_TRUSTED_CLIENT")
        .scopes("read", "write")
        .secret(this.passwordEncoder.encode(oauthProperties.getClientSecret()))
        .accessTokenValiditySeconds(oauthProperties.getTokenValidityDays() * 24 * 60 * 60)
        .refreshTokenValiditySeconds(oauthProperties.getTokenValidityDays() * 24 * 60 * 60)
    ;
  }

  /**
   * Token store token store.
   *
   * @return the token store
   */
  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  /**
   * Access token converter jwt access token converter.
   *
   * @return the jwt access token converter
   */
  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setSigningKey(webSecurityProperties.getOauth().getTokenSigningKey());
    return converter;
  }

  /**
   * Token services default token services.
   *
   * @return the default token services
   */
  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());
    defaultTokenServices.setSupportRefreshToken(true);
    return defaultTokenServices;
  }
}
