package io.trabricks.boot.data.jpa.support;

import io.trabricks.boot.data.jpa.domain.BaseEntity;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

/**
 * The type Data tables service.
 *
 * @author eomjeongjae
 * @since 2019 -02-22
 */
public class DataTablesService {

  private final ModelMapper modelMapper;

  /**
   * Instantiates a new Data tables service.
   *
   * @param modelMapper the model mapper
   */
  public DataTablesService(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  /**
   * Convert data tables output.
   *
   * @param <T>             the type parameter
   * @param source          the source
   * @param destinationType the destination type
   * @return the data tables output
   */
  public <T> DataTablesOutput<T> convert(
      DataTablesOutput<? extends BaseEntity> source, Class<T> destinationType) {
    DataTablesOutput<T> output = new DataTablesOutput();
    output.setDraw(source.getDraw());
    output.setRecordsTotal(source.getRecordsTotal());
    output.setRecordsFiltered(source.getRecordsFiltered());
    output.setError(source.getError());
    output.setData(source.getData().stream()
        .map(post -> modelMapper.map(post, destinationType))
        .collect(Collectors.toList()));
    return output;
  }
}
