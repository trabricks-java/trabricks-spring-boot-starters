package io.trabricks.boot.data.jpa.converters;

import java.util.Set;
import javax.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Set attribute converter.
 *
 * @author eomjeongjae
 * @since 2019 -02-27
 */
@Slf4j
@Converter(autoApply = true)
public class SetAttributeConverter extends ObjectAttributeConverter<Set> {

  @Override
  protected Class<Set> getInstance() {
    return Set.class;
  }

}

