package com.trabricks.security.properties;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author eomjeongjae
 * @since 25/09/2019
 */
@ConfigurationProperties(prefix = "trabricks.security")
@Getter
@Setter
@ToString
public class WebSecurityProperties {

  private Rest rest = new Rest();

  @Getter
  @Setter
  @ToString
  public static class Rest {

    @NotNull
    private boolean enabled;

  }

}