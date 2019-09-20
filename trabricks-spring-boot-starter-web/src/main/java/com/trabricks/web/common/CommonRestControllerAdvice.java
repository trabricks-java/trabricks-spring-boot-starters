package com.trabricks.web.common;

import com.trabricks.web.common.ErrorResponse.FieldError;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Jeongjae Eom
 * @since 2019-01-03
 */
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations = RestController.class)
public class CommonRestControllerAdvice extends ResponseEntityExceptionHandler {

  private final ModelMapper modelMapper;

  @Override
  protected ResponseEntity handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    return errorResponseEntity(status, ex, ex.getBindingResult());
  }

  @Override
  protected ResponseEntity<Object> handleBindException(
      BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return errorResponseEntity(status, ex, ex.getBindingResult());
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    return errorResponseEntity(status, ex);
  }

  @ExceptionHandler({AccessDeniedException.class})
  protected ResponseEntity<Object> handleAccessDeniedException(final Exception ex,
      final WebRequest request) {
    log.info("request.getUserPrincipal(): {}", request.getUserPrincipal());
    return errorResponseEntity(HttpStatus.FORBIDDEN, ex);
  }

  @ExceptionHandler({IllegalArgumentException.class})
  protected ResponseEntity<Object> handleIllegalArgumentException(Exception ex) {
    return errorResponseEntity(HttpStatus.BAD_REQUEST, ex);
  }

  @ExceptionHandler(HttpStatusException.class)
  protected ResponseEntity<Object> handleHttpStatusException(HttpStatusException ex) {
    return errorResponseEntity(ex.getStatus(), ex);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleException(Exception ex, HttpServletRequest request) {
    HttpStatus status = getStatus(request);
    return errorResponseEntity(status, ex);
  }

  private ResponseEntity errorResponseEntity(HttpStatus status, Exception ex) {
    return this.errorResponseEntity(status, ex, null, null);
  }

  private ResponseEntity errorResponseEntity(HttpStatus status, Exception ex,
      BindingResult bindingResult) {
    return this.errorResponseEntity(status, ex, null, bindingResult);
  }

  private ResponseEntity errorResponseEntity(HttpStatus status, Exception ex, String code) {
    return this.errorResponseEntity(status, ex, code, null);
  }

  private ResponseEntity errorResponseEntity(HttpStatus status, Exception ex, String code,
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
            .message(ex.getMessage())
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