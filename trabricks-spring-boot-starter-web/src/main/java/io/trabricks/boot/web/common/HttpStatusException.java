package io.trabricks.boot.web.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * The type Http status exception.
 *
 * @author eomjeongjae
 * @since 2019 -08-12
 */
@Getter
public class HttpStatusException extends RuntimeException {

  private final HttpStatus status;
  private final String code;

  /**
   * Instantiates a new Http status exception.
   *
   * @param status the status
   * @param msg    the msg
   */
  public HttpStatusException(HttpStatus status, String msg) {
    this(status, msg, null);
  }

  /**
   * Instantiates a new Http status exception.
   *
   * @param status the status
   * @param msg    the msg
   * @param code   the code
   */
  public HttpStatusException(HttpStatus status, String msg, String code) {
    super(msg);
    this.status = status;
    this.code = code;
  }

  /**
   * Instantiates a new Http status exception.
   *
   * @param status the status
   * @param msg    the msg
   * @param code   the code
   * @param t      the t
   */
  public HttpStatusException(HttpStatus status, String msg, String code, Throwable t) {
    super(msg, t);
    this.status = status;
    this.code = code;
  }

}
