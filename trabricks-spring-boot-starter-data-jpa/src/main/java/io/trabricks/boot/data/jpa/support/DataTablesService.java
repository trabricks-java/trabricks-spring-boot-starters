package io.trabricks.boot.data.jpa.support;

import io.trabricks.boot.data.jpa.domain.BaseEntity;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

/**
 * @author eomjeongjae
 * @since 2019-02-22
 */
public class DataTablesService {

  private final ModelMapper modelMapper;

  public DataTablesService(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

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
