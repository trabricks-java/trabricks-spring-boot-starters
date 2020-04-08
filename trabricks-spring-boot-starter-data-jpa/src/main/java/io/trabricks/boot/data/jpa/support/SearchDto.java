package io.trabricks.boot.data.jpa.support;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ObjectUtils;

/**
 * The type Search dto.
 *
 * @param <T> the type parameter
 * @author eomjeongjae
 * @since 2019 -07-23
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public abstract class SearchDto<T> {

  /**
   * The Period type.
   */
  @Setter
  @Default
  protected String periodType = "createdAt";

  /**
   * The From date.
   */
  @Setter
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  protected LocalDate fromDate;

  /**
   * The To date.
   */
  @Setter
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  protected LocalDate toDate;

  /**
   * To specification specification.
   *
   * @return the specification
   */
  public final Specification<T> toSpecification() {
    return getRestrictions().output();
  }

  /**
   * Gets restrictions.
   *
   * @return the restrictions
   */
  public final Restrictions getRestrictions() {
    final Restrictions restrictions = this.generateRestrictions();
    if (!ObjectUtils.isEmpty(this.fromDate) && !ObjectUtils.isEmpty(this.toDate)) {
      restrictions.between(this.periodType, LocalDateTime.of(this.fromDate, LocalTime.MIN),
          LocalDateTime.of(this.toDate, LocalTime.MAX));
    }
    return restrictions;
  }

  /**
   * Generate restrictions restrictions.
   *
   * @return the restrictions
   */
  protected abstract Restrictions generateRestrictions();

}
