package com.trabricks.web.interceptors;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author eomjeongjae
 * @since 2019-02-08
 */
@Slf4j
public class WebInterceptor implements HandlerInterceptor {

  private String getParameters(HttpServletRequest request) {
    StringBuffer posted = new StringBuffer();
    Enumeration<?> e = request.getParameterNames();
    if (e != null) {
      posted.append("?");
    }
    while (e.hasMoreElements()) {
      if (posted.length() > 1) {
        posted.append("&");
      }
      String curr = (String) e.nextElement();
      posted.append(curr + "=");
      if (curr.contains("password")
          || curr.contains("pass")
          || curr.contains("pwd")) {
        posted.append("*****");
      } else {
        posted.append(request.getParameter(curr));
      }
    }
    String ip = request.getHeader("X-FORWARDED-FOR");
    String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
    if (ipAddr != null && !ipAddr.equals("")) {
      posted.append("&_psip=" + ipAddr);
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

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    String usrAgent = request.getHeader("User-Agent");
    if (usrAgent != null && !usrAgent.isEmpty() && usrAgent.contains("MSIE")) {
      String v = usrAgent.substring(usrAgent.indexOf("MSIE") + 4).trim();
      v = v.substring(0, v.indexOf(";"));
      if (Float.parseFloat(v) <= 9.0) {
        modelAndView.setViewName("shared/barricade");
      }
    }
    log.info("[postHandle][{}][{}{}]", request.getMethod(), request.getRequestURI(),
        getParameters(request));
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    if (ex != null) {
      ex.printStackTrace();
      log.info("[afterCompletion][{}][{}{}][exception: {}]", request.getMethod(),
          request.getMethod(), request.getRequestURI(), ex);
    }
  }
}
