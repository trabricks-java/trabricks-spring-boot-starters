package com.trabricks.security.configs;

import com.trabricks.security.properties.WebSecurityProperties;
import com.trabricks.security.properties.WebSecurityProperties.Oauth;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SpringBootWebSecurityConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
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
 * @author eomjeongjae
 * @since 29/09/2019
 */

@RequiredArgsConstructor
@Configuration
@AutoConfigureAfter(SpringBootWebSecurityConfiguration.class)
@ConditionalOnProperty(prefix = "trabricks.security", name = "oauth.enabled", havingValue = "true")
@EnableAuthorizationServer
@EnableConfigurationProperties(WebSecurityProperties.class)
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final WebSecurityProperties webSecurityProperties;

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints
        .tokenStore(tokenStore())
        .accessTokenConverter(accessTokenConverter())
        .authenticationManager(authenticationManager);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()")
        .allowFormAuthenticationForClients();
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    Oauth oauthProperties = webSecurityProperties.getOauth();
    clients.inMemory()
        .withClient(oauthProperties.getClientId())
        .authorizedGrantTypes("password", "refresh_token", "client_credentials")
        .scopes("read", "write")
        .secret(this.passwordEncoder.encode(oauthProperties.getClientSecret()))
        .accessTokenValiditySeconds(oauthProperties.getTokenValidityDays() * 24 * 60 * 60)
        .refreshTokenValiditySeconds((oauthProperties.getTokenValidityDays() + 1) * 24 * 60 * 60);
  }

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setSigningKey(webSecurityProperties.getOauth().getTokenSigningKey());
    return converter;
  }

  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());
    defaultTokenServices.setSupportRefreshToken(true);
    return defaultTokenServices;
  }
}
