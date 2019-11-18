package com.trabricks.data.jpa.support;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
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

  @ApiModelProperty(position = 98)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate fromDate = LocalDate.now().minusMonths(5);

  @ApiModelProperty(position = 99)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate toDate = LocalDate.now();

  @ApiModelProperty(hidden = true)
  private LocalDateTime fromDateTime = LocalDateTime
      .of(LocalDate.now().minusMonths(5), LocalTime.MIN);

  @ApiModelProperty(hidden = true)
  private LocalDateTime toDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

  public void setFromDate(LocalDate fromDate) {
    this.fromDate = fromDate;
    this.fromDateTime = LocalDateTime.of(fromDate, LocalTime.MIN);
  }

  public void setToDate(LocalDate toDate) {
    this.toDate = toDate;
    this.toDateTime = LocalDateTime.of(toDate, LocalTime.MAX);
  }

  public final Specification<T> toSpecification() {
    final Restrictions restrictions = this.generateRestrictions();
    if (!ObjectUtils.isEmpty(this.fromDateTime) && !ObjectUtils.isEmpty(this.toDateTime)) {
      restrictions.between("createdAt", this.fromDateTime, this.toDateTime);
    }

    return restrictions.output();
  }

  protected abstract Restrictions generateRestrictions();

}
