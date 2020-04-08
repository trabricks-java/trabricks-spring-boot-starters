package io.trabricks.boot.web.notice.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The type Default firebase message.
 *
 * @author eomjeongjae
 * @since 2019 /10/31
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DefaultFirebaseMessage implements FirebaseMessage {

  private String title;
  private String body;
  private String link;
  private String token;

}
