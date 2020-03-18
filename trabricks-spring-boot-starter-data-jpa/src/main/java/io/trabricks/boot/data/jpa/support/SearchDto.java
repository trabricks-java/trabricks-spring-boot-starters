package io.trabricks.boot.data.jpa.support;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ObjectUtils;

/**
 * @author eomjeongjae
 * @since 2019-07-23
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public abstract class SearchDto<T> {

  @Setter
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  protected LocalDate fromDate;

  @Setter
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  protected LocalDate toDate;

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
