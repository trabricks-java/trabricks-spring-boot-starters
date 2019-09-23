package com.trabricks.data.jpa.support;

import java.util.Map;
import javax.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author eomjeongjae
 * @since 2019-02-27
 */
@Slf4j
@Converter(autoApply = true)
public class MapAttributeConverter extends ObjectAttributeConverter<Map> {

  @Override
  protected Class<Map> getInstance() {
    return Map.class;
  }

}
