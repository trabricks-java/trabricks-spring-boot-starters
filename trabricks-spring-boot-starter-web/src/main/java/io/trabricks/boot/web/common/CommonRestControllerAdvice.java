package io.trabricks.boot.web.common;

import io.trabricks.boot.web.common.ErrorResponse.FieldError;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The type Common rest controller advice.
 *
 * @author Jeongjae Eom
 * @since 2019 -01-03
 */
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations = RestController.class)
public class CommonRestControllerAdvice extends ResponseEntityExceptionHandler {

  private final MessageSourceAccessor messageSourceAccessor;
  private final ModelMapper modelMapper;

  @Override
  protected ResponseEntity handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return errorResponseEntity(status, ex, "Method argument not valid", null,
        ex.getBindingResult());
  }

  @Override
  protected ResponseEntity<Object> handleBindException(
      BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return errorResponseEntity(status, ex, "Method argument binding error", null,
        ex.getBindingResult());
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    return errorResponseEntity(status, ex);
  }

  /**
   * Handle access denied exception response entity.
   *
   * @param ex      the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler({AccessDeniedException.class})
  protected ResponseEntity<Object> handleAccessDeniedException(final Exception ex,
      final WebRequest request) {
    log.info("request.getUserPrincipal(): {}", request.getUserPrincipal());
    return errorResponseEntity(HttpStatus.FORBIDDEN, ex);
  }

  /**
   * Handle authentication exception response entity.
   *
   * @param ex      the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler({AuthenticationException.class})
  protected ResponseEntity<Object> handleAuthenticationException(final Exception ex,
      final WebRequest request) {
    return errorResponseEntity(HttpStatus.UNAUTHORIZED, ex);
  }

  /**
   * Handle illegal argument exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler({IllegalArgumentException.class})
  protected ResponseEntity<Object> handleIllegalArgumentException(Exception ex) {
    return errorResponseEntity(HttpStatus.BAD_REQUEST, ex);
  }

  /**
   * Handle http status exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(HttpStatusException.class)
  protected ResponseEntity<Object> handleHttpStatusException(HttpStatusException ex) {
    return errorResponseEntity(ex.getStatus(), ex, ex.getCode());
  }

  /**
   * Handle http status message exception response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(HttpStatusMessageException.class)
  protected ResponseEntity<Object> handleHttpStatusMessageException(HttpStatusMessageException ex) {
    String code = ex.getCode();

    String responseMsg = null;
    if (!StringUtils.isEmpty(code)) {
      try {
        responseMsg = messageSourceAccessor.getMessage(code, ex.getArgs());
      } catch (Exception e) {
        // nothing
        log.error("No such message", ex);
      }
    }

    return errorResponseEntity(ex.getStatus(), ex, responseMsg, ex.getCode(), null);
  }

  /**
   * Handle exception response entity.
   *
   * @param ex      the ex
   * @param request the request
   * @return the response entity
   */
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleException(Exception ex, HttpServletRequest request) {
    HttpStatus status = getStatus(request);
    return errorResponseEntity(status, ex);
  }

  private ResponseEntity errorResponseEntity(HttpStatus status, Exception ex) {
    return this.errorResponseEntity(status, ex, null, null, null);
  }

  private ResponseEntity errorResponseEntity(HttpStatus status, Exception ex,
      BindingResult bindingResult) {
    return this.errorResponseEntity(status, ex, null, null, bindingResult);
  }

  private ResponseEntity errorResponseEntity(HttpStatus status, Exception ex, String code) {
    return this.errorResponseEntity(status, ex, null, code, null);
  }

  private ResponseEntity errorResponseEntity(
      HttpStatus status,
      Exception ex,
      String message,
      String code,
      BindingResult bindingResult) {
    log.error("Error occurred", ex);

    List<FieldError> fieldErrors = null;
    if (!ObjectUtils.isEmpty(bindingResult)) {
      fieldErrors = getFieldErrors(bindingResult);
    }

    return ResponseEntity
        .status(status)
        .body(ErrorResponse.builder()
            .status(status)
            .code(Objects.toString(code, ex.getClass().getSimpleName()))
            .message(Objects.toString(message, ex.getMessage()))
            .fieldError(fieldErrors)
            .build());
  }

  private List<FieldError> getFieldErrors(BindingResult bindingResult) {
    return modelMapper.map(bindingResult.getFieldErrors(), new TypeToken<List<FieldError>>() {
    }.getType());
  }

  private HttpStatus getStatus(HttpServletRequest request) {
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    log.info("statusCode: {}", statusCode);
    if (statusCode == null) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return HttpStatus.valueOf(statusCode);
  }
}
