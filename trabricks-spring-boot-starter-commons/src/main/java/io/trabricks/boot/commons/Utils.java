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
 * @author eomjeongjae
 * @since 2019-02-11
 */
public class Utils {

  public static String toOnlyNumber(String str) {
    if (StringUtils.isNotEmpty(str)) {
      return str.replaceAll("[^\\d.]", "");
    }
    return str;
  }

  public static String toRemoveMarkup(String str) {
    if (StringUtils.isNotEmpty(str)) {
      return str.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
    }
    return str;
  }

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

  public static String abbreviate(String str, String maxWidth) {
    return StringUtils.abbreviate(str, Integer.valueOf(maxWidth));
  }

  public static String toDelayTime(LocalDateTime questionTime, LocalDateTime answerTime) {
    long until = questionTime.until(answerTime, ChronoUnit.SECONDS);
    return String.format("%d분 %d초",
        TimeUnit.SECONDS.toMinutes(until),
        TimeUnit.SECONDS.toSeconds(until) -
            TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(until))
    );
  }

  public static String generateState() {
    SecureRandom random = new SecureRandom();
    return new BigInteger(130, random).toString(32);
  }

  public static String toDayOfWeekDisplayName(DayOfWeek dayOfWeek) {
    return dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREA);
  }

  public static String toDayOfWeekDisplayNameByEnglish(DayOfWeek dayOfWeek) {
    return dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.ENGLISH);
  }

  public static String nvl(Object o, String nullDefault) {
    return Objects.toString(o, nullDefault);
  }

  public static <T extends Enum<T>> Enum<T> toEnum(Class<T> enumClass, String value) {
    return Enum.valueOf(enumClass, value);
  }

  public static String toPrivateStr(String str) {
    if (StringUtils.isNotEmpty(str)) {
      return str.replaceAll("[0-9]", "*");
    }
    return str;
  }

  public Long toLong(String str) {
    return Long.parseLong(str);
  }

}
