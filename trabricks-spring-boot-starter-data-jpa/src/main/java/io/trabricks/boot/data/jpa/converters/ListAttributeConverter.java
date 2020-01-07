package io.trabricks.boot.data.jpa.converters;

import java.util.List;
import javax.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author eomjeongjae
 * @since 2019-02-27
 */
@Slf4j
@Converter(autoApply = true)
public class ListAttributeConverter extends ObjectAttributeConverter<List> {

  @Override
  protected Class<List> getInstance() {
    return List.class;
  }

}

