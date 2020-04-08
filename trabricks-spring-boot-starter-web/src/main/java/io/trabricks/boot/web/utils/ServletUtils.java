package io.trabricks.boot.web.utils;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * The type Servlet utils.
 *
 * @author eomjeongjae
 * @since 2019 -09-24
 */
public class ServletUtils {

  /**
   * Gets current request.
   *
   * @return the current request
   */
  public static HttpServletRequest getCurrentRequest() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes instanceof ServletRequestAttributes) {
      HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
      return request;
    }
    return null;
  }

}
