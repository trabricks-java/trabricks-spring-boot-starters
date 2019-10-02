package com.trabricks.data.jpa.domain;

import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * @author eomjeongjae
 * @since 2019-02-01
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class AuditableEntity<T> extends BaseEntity {

  @CreatedBy
  private T createdBy;

  @LastModifiedBy
  private T lastModifiedBy;

}