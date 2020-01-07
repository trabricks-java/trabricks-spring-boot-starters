package io.trabricks.boot.web.storage.exceptions;

/**
 * @author eomjeongjae
 * @since 2019/10/15
 */
public class StorageFileNotFoundException extends StorageException {

  public StorageFileNotFoundException(String message) {
    super(message);
  }

  public StorageFileNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
