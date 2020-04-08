package io.trabricks.boot.web.common;

import io.trabricks.boot.commons.HtmlUtils;
import io.trabricks.boot.commons.Utils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * The type Abstract controller advice.
 *
 * @author Jeongjae Eom
 * @since 2019 -01-03
 */
@Slf4j
abstract public class AbstractControllerAdvice {

  @Autowired
  private Environment environment;

  /**
   * Add model attribute.
   *
   * @param model the model
   */
  public void addModelAttribute(ModelMap model) {
    model.addAttribute("environment", environment);
    model.addAttribute("isProduct", environment.acceptsProfiles(Profiles.of("prod")));
    model.addAttribute("StringUtils", new StringUtils());
    model.addAttribute("Utils", new Utils());
    model.addAttribute("HtmlUtils", new HtmlUtils());
  }

  /**
   * Exception string.
   *
   * @param request  the request
   * @param response the response
   * @param ex       the ex
   * @return the string
   */
  @ExceptionHandler(Exception.class)
  public String exception(HttpServletRequest request, HttpServletResponse response, Exception ex) {
    log.error("Exception occurred", ex);
    response.setStatus(getStatus(request).value());
    return "/error";
  }

  private HttpStatus getStatus(HttpServletRequest request) {
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    if (statusCode == null) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return HttpStatus.valueOf(statusCode);
  }

}
