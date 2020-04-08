package io.trabricks.boot.commons;

import java.math.BigInteger;
import java.net.URI;
import java.security.SecureRandom;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;

/**
 * The type Utils.
 *
 * @author eomjeongjae
 * @since 2019 -02-11
 */
public class Utils {

  /**
   * To only number string.
   *
   * @param str the str
   * @return the string
   */
  public static String toOnlyNumber(String str) {
    if (StringUtils.isNotEmpty(str)) {
      return str.replaceAll("[^\\d.]", "");
    }
    return str;
  }

  /**
   * To remove markup string.
   *
   * @param str the str
   * @return the string
   */
  public static String toRemoveMarkup(String str) {
    if (StringUtils.isNotEmpty(str)) {
      return str.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
    }
    return str;
  }

  /**
   * To add param string.
   *
   * @param baseUri the base uri
   * @param query   the query
   * @return the string
   */
  public static String toAddParam(String baseUri, String query) {
    if (StringUtils.isEmpty(baseUri)) {
      return baseUri;
    }

    URI uri = URI.create(baseUri);
    if (StringUtils.isNotEmpty(uri.getQuery())) {
      return uri + "&" + query;
    }
    return uri + "?" + query;
  }

  /**
   * Abbreviate string.
   *
   * @param str      the str
   * @param maxWidth the max width
   * @return the string
   */
  public static String abbreviate(String str, String maxWidth) {
    return StringUtils.abbreviate(str, Integer.valueOf(maxWidth));
  }

  /**
   * To delay time string.
   *
   * @param questionTime the question time
   * @param answerTime   the answer time
   * @return the string
   */
  public static String toDelayTime(LocalDateTime questionTime, LocalDateTime answerTime) {
    long until = questionTime.until(answerTime, ChronoUnit.SECONDS);
    return String.format("%d분 %d초",
        TimeUnit.SECONDS.toMinutes(until),
        TimeUnit.SECONDS.toSeconds(until) -
            TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(until))
    );
  }

  /**
   * Generate state string.
   *
   * @return the string
   */
  public static String generateState() {
    SecureRandom random = new SecureRandom();
    return new BigInteger(130, random).toString(32);
  }

  /**
   * To day of week display name string.
   *
   * @param dayOfWeek the day of week
   * @return the string
   */
  public static String toDayOfWeekDisplayName(DayOfWeek dayOfWeek) {
    return dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREA);
  }

  /**
   * To day of week display name by english string.
   *
   * @param dayOfWeek the day of week
   * @return the string
   */
  public static String toDayOfWeekDisplayNameByEnglish(DayOfWeek dayOfWeek) {
    return dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.ENGLISH);
  }

  /**
   * Nvl string.
   *
   * @param o           the o
   * @param nullDefault the null default
   * @return the string
   */
  public static String nvl(Object o, String nullDefault) {
    return Objects.toString(o, nullDefault);
  }

  /**
   * To enum enum.
   *
   * @param <T>       the type parameter
   * @param enumClass the enum class
   * @param value     the value
   * @return the enum
   */
  public static <T extends Enum<T>> Enum<T> toEnum(Class<T> enumClass, String value) {
    return Enum.valueOf(enumClass, value);
  }

  /**
   * To private str string.
   *
   * @param str the str
   * @return the string
   */
  public static String toPrivateStr(String str) {
    if (StringUtils.isNotEmpty(str)) {
      return str.replaceAll("[0-9]", "*");
    }
    return str;
  }

  /**
   * To long long.
   *
   * @param str the str
   * @return the long
   */
  public Long toLong(String str) {
    return Long.parseLong(str);
  }

}
