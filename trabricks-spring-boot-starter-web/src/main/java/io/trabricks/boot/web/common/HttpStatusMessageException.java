package io.trabricks.boot.web.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpStatusMessageException extends HttpStatusException {

  private Object[] args;

  public HttpStatusMessageException(HttpStatus status, String code, Object... args) {
    super(status, "Http status message exception", code);
    this.args = args;
  }
}
