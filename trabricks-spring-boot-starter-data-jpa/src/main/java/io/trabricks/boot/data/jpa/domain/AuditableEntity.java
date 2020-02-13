package io.trabricks.boot.data.jpa.domain;

import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * @author eomjeongjae
 * @since 2019-02-01
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public abstract class AuditableEntity<T> extends BaseEntity {

  @CreatedBy
  private T createdBy;

  @LastModifiedBy
  private T lastModifiedBy;

}
