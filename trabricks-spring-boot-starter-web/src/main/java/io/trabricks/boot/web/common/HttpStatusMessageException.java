package io.trabricks.boot.web.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * The type Http status message exception.
 */
@Getter
public class HttpStatusMessageException extends HttpStatusException {

  private Object[] args;

  /**
   * Instantiates a new Http status message exception.
   *
   * @param status the status
   * @param code   the code
   * @param args   the args
   */
  public HttpStatusMessageException(HttpStatus status, String code, Object... args) {
    super(status, "Http status message exception", code);
    this.args = args;
  }
}
