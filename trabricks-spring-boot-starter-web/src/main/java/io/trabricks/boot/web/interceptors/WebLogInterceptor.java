package io.trabricks.boot.web.interceptors;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * The type Web log interceptor.
 *
 * @author eomjeongjae
 * @since 2019 -02-08
 */
@Slf4j
public class WebLogInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler
  ) {
    log.info(
        "[preHandle][{}][{}{}]",
        request.getMethod(),
        request.getRequestURI(),
        getParameters(request)
    );
    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      Exception ex
  ) {
    if (ex != null) {
      ex.printStackTrace();
      log.info(
          "[afterCompletion][{}][{}{}][exception: {}]",
          request.getMethod(),
          request.getMethod(),
          request.getRequestURI(),
          ex
      );
    }
  }

  private String getParameters(HttpServletRequest request) {
    StringBuilder posted = new StringBuilder();
    Enumeration<?> e = request.getParameterNames();
    if (e != null) {
      posted.append("?");
      while (e.hasMoreElements()) {
        if (posted.length() > 1) {
          posted.append("&");
        }

        String curr = (String) e.nextElement();
        posted.append(curr).append("=");
        if (curr.contains("password")
            || curr.contains("pass")
            || curr.contains("pwd")) {
          posted.append("*****");
        } else {
          posted.append(request.getParameter(curr));
        }
      }
    }

    String ip = request.getHeader("X-FORWARDED-FOR");
    String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
    if (ipAddr != null && !ipAddr.equals("")) {
      posted.append("&_clientIp=").append(ipAddr);
    }

    return posted.toString();
  }

  private String getRemoteAddr(HttpServletRequest request) {
    String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
    if (ipFromHeader != null && ipFromHeader.length() > 0) {
      log.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
      return ipFromHeader;
    }
    return request.getRemoteAddr();
  }
}
