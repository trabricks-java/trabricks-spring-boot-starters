package io.trabricks.boot.data.jpa.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.AttributeConverter;

/**
 * The type Object attribute converter.
 *
 * @param <T> the type parameter
 * @author eomjeongjae
 * @since 2019 -02-27
 */
public abstract class ObjectAttributeConverter<T> implements AttributeConverter<T, String> {

  private final static ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Gets instance.
   *
   * @return the instance
   */
  protected abstract Class<T> getInstance();

  @Override
  public String convertToDatabaseColumn(Object attribute) {
    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (Exception e) {
    }
    return null;
  }

  @Override
  public T convertToEntityAttribute(String dbData) {
    try {
      return objectMapper.readValue(dbData, getInstance());
    } catch (Exception e) {
    }
    return null;
  }
}
