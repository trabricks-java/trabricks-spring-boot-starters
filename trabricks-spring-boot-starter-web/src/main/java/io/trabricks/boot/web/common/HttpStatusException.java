package io.trabricks.boot.web.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author eomjeongjae
 * @since 2019-08-12
 */
@Getter
public class HttpStatusException extends RuntimeException{

  private final HttpStatus status;
  private final String code;

  public HttpStatusException(HttpStatus status, String msg) {
    this(status, msg, null);
  }

  public HttpStatusException(HttpStatus status, String msg, String code) {
    super(msg);
    this.status = status;
    this.code = code;
  }

  public HttpStatusException(HttpStatus status, String msg, String code, Throwable t) {
    super(msg, t);
    this.status = status;
    this.code = code;
  }
}
