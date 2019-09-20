package com.trabricks.web.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author eomjeongjae
 * @since 2019-08-12
 */
@Getter
public class HttpStatusException extends RuntimeException{

  private final HttpStatus status;

  public HttpStatusException(HttpStatus status, String msg) {
    super(msg);
    this.status = status;
  }

  public HttpStatusException(HttpStatus status, String msg, Throwable t) {
    super(msg, t);
    this.status = status;
  }
}
