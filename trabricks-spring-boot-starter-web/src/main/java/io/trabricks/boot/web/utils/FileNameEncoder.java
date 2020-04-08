package io.trabricks.boot.web.utils;

import eu.bitwalker.useragentutils.Browser;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * The enum File name encoder.
 */
public enum FileNameEncoder {
  /**
   * The Ie.
   */
  IE(Browser.IE, it -> {
    try {
      return URLEncoder.encode(it, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
    } catch (UnsupportedEncodingException e) {
      return it;
    }
  }),
  /**
   * Firefox file name encoder.
   */
  FIREFOX(Browser.FIREFOX, getDefaultEncodeOperator()),
  /**
   * Opera file name encoder.
   */
  OPERA(Browser.OPERA, getDefaultEncodeOperator()),
  /**
   * Chrome file name encoder.
   */
  CHROME(Browser.CHROME, getDefaultEncodeOperator()),
  /**
   * Unknown file name encoder.
   */
  UNKNOWN(Browser.UNKNOWN, UnaryOperator.identity());

  private final Browser browser;
  private UnaryOperator<String> encodeOperator;

  private static final Map<Browser, Function<String, String>> FILE_NAME_ENCODER_MAP;

  static {
    FILE_NAME_ENCODER_MAP = EnumSet.allOf(FileNameEncoder.class).stream()
        .collect(
            Collectors.toMap(FileNameEncoder::getBrowser, FileNameEncoder::getEncodeOperator));
  }

  FileNameEncoder(Browser browser, UnaryOperator<String> encodeOperator) {
    this.browser = browser;
    this.encodeOperator = encodeOperator;
  }

  /**
   * Gets browser.
   *
   * @return the browser
   */
  protected Browser getBrowser() {
    return browser;
  }

  /**
   * Gets encode operator.
   *
   * @return the encode operator
   */
  protected UnaryOperator<String> getEncodeOperator() {
    return encodeOperator;
  }

  private static UnaryOperator<String> getDefaultEncodeOperator() {
    return it -> new String(it.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
  }

  /**
   * Encode string.
   *
   * @param browser  the browser
   * @param fileName the file name
   * @return the string
   */
  public static String encode(Browser browser, String fileName) {
    return FILE_NAME_ENCODER_MAP.get(browser).apply(fileName);
  }
}
