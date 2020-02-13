package io.trabricks.boot.data.jpa.domain;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public abstract class AuditableUniqueEntity<T> extends AuditableEntity<T> {

  @Include
  @Column(unique = true, nullable = false)
  private String uniqueId = UUID.randomUUID().toString();

}
