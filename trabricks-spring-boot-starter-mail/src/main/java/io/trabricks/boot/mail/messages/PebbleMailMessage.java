package io.trabricks.boot.mail.messages;

import com.google.common.collect.Maps;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author eomjeongjae
 * @since 2019-06-27
 */
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PebbleMailMessage extends MailMessage {

  private final String templateName;
  private final Map<String, Object> attributes;

  public PebbleMailMessage(String templateName) {
    this.templateName = templateName;
    this.attributes = Maps.newHashMap();
  }

  public PebbleMailMessage addAttribute(String key, Object value) {
    this.attributes.put(key, value);
    return this;
  }

  public PebbleMailMessage removeAttribute(String key) {
    if (this.attributes.containsKey(key)) {
      this.attributes.remove(key);
    }
    return this;
  }

  public Map<String, Object> getAttributes() {
    return java.util.Collections.unmodifiableMap(this.attributes);
  }
}
