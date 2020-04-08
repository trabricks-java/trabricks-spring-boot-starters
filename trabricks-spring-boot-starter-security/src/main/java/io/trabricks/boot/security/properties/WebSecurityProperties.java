package io.trabricks.boot.security.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Web security properties.
 *
 * @author eomjeongjae
 * @since 25 /09/2019
 */
@ConfigurationProperties(prefix = "trabricks.security")
@Getter
@Setter
@ToString
public class WebSecurityProperties {

  private Rest rest = new Rest();
  private Oauth oauth = new Oauth();

  /**
   * The type Rest.
   */
  @Getter
  @Setter
  @ToString
  public static class Rest {

    private boolean enabled;

  }

  /**
   * The type Oauth.
   */
  @Getter
  @Setter
  @ToString
  public static class Oauth {

    private boolean enabled;
    private String clientId;
    private String clientSecret;
    private String tokenSigningKey;
    private int tokenValidityDays = 1;
    private int refreshTokenValidityDays = 10;

  }

}
