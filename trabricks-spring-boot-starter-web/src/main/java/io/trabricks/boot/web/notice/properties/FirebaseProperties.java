package io.trabricks.boot.web.notice.properties;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author eomjeongjae
 * @since 2019/10/31
 */
@Setter
@Getter
@ToString
@Component
@ConfigurationProperties("firebase")
public class FirebaseProperties {

  private String projectId;
  private String baseUrl;
  private String fcmSendEndpoint;
  private String messagingScope;
  private List<String> scopes;
  private String messageKey;
  private String host;

  @NotEmpty
  private String privateKeyPath;

  @NotEmpty
  private String databaseUrl;

}
