package io.trabricks.boot.mail.messages;

import com.google.common.collect.Lists;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.mail.SimpleMailMessage;

/**
 * @author eomjeongjae
 * @since 2019-06-27
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MailMessage extends SimpleMailMessage {

  private List<File> attachments;
  private String encoding;
  private boolean htmlContent;

  public MailMessage() {
    super();
    super.setSentDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    this.attachments = Lists.newArrayList();
  }

  public MailMessage addAttachment(File file) {
    if (null != file) {
      this.attachments.add(file);
    }
    return this;
  }

  public MailMessage removeAttachment(File file) {
    if (null != file && this.attachments.contains(file)) {
      this.attachments.remove(file);
    }
    return this;
  }

  public List<File> getAttachments() {
    return Collections.unmodifiableList(this.attachments);
  }

  public boolean isMultipart() {
    return !this.attachments.isEmpty();
  }

}
