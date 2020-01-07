package io.trabricks.boot.web.storage.exceptions;

/**
 * @author eomjeongjae
 * @since 2019/10/15
 */
public class StorageException extends RuntimeException {

  public StorageException(String message) {
    super(message);
  }

  public StorageException(String message, Throwable cause) {
    super(message, cause);
  }
}
