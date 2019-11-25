package com.trabricks.data.jpa.support;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ObjectUtils;

/**
 * @author eomjeongjae
 * @since 2019-07-23
 */
@Getter
@ToString
public abstract class SearchDto<T> {

  @Setter
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate fromDate;

  @Setter
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate toDate;

  public final Specification<T> toSpecification() {
    return getRestrictions().output();
  }

  public final Restrictions getRestrictions() {
    final Restrictions restrictions = this.generateRestrictions();
    if (!ObjectUtils.isEmpty(this.fromDate) && !ObjectUtils.isEmpty(this.toDate)) {
      restrictions.between("createdAt", LocalDateTime.of(this.fromDate, LocalTime.MIN),
          LocalDateTime.of(this.toDate, LocalTime.MAX));
    }
    return restrictions;
  }

  protected abstract Restrictions generateRestrictions();

}
