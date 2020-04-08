package io.trabricks.boot.web.notice.service;

/**
 * The interface Firebase message.
 *
 * @author eomjeongjae
 * @since 2019 -09-24
 */
public interface FirebaseMessage {

  /**
   * Gets title.
   *
   * @return the title
   */
  String getTitle();

  /**
   * Gets body.
   *
   * @return the body
   */
  String getBody();

  /**
   * Gets link.
   *
   * @return the link
   */
  String getLink();

  /**
   * Gets token.
   *
   * @return the token
   */
  String getToken();

}
