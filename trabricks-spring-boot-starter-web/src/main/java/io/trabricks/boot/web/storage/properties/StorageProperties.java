package io.trabricks.boot.web.storage.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author eomjeongjae
 * @since 2019/10/15
 */
@Getter
@Setter
@ToString
@ConfigurationProperties("storage")
public class StorageProperties {

  private String location;

}
