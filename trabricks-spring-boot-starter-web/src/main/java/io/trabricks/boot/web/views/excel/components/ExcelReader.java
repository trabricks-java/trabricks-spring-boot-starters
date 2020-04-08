package io.trabricks.boot.web.views.excel.components;

import io.trabricks.boot.web.views.excel.constants.Excel;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Excel reader.
 */
@Slf4j
public class ExcelReader {

  /**
   * Read file to list list.
   *
   * @param <T>           the type parameter
   * @param multipartFile the multipart file
   * @param rowFunc       the row func
   * @return the list
   * @throws IOException            the io exception
   * @throws InvalidFormatException the invalid format exception
   */
  public <T> List<T> readFileToList(
      final MultipartFile multipartFile,
      final Function<Row, T> rowFunc
  ) throws IOException, InvalidFormatException {

    final Workbook workbook = readWorkbook(multipartFile);
    final Sheet sheet = workbook.getSheetAt(0);
    final int rowCount = sheet.getPhysicalNumberOfRows();

    return IntStream
        .range(1, rowCount)
        .mapToObj(rowIndex -> rowFunc.apply(sheet.getRow(rowIndex)))
        .collect(Collectors.toList());
  }

  private Workbook readWorkbook(MultipartFile multipartFile) throws IOException, InvalidFormatException {
    verifyFileExtension(multipartFile);
    return multipartFileToWorkbook(multipartFile);
  }

  private void verifyFileExtension(MultipartFile multipartFile) throws InvalidFormatException {
    final String originalFilename = multipartFile.getOriginalFilename();
    log.info("originalFilename: {}", originalFilename);
    if (!isExcelExtension(originalFilename)) {
      throw new InvalidFormatException("This file extension is not verify");
    }
  }

  private boolean isExcelExtension(String fileName) {
    return fileName.endsWith(Excel.XLS.getName()) || fileName.endsWith(Excel.XLSX.getName());
  }

  private boolean isExcelXls(String fileName) {
    return fileName.endsWith(Excel.XLS.getName());
  }

  private boolean isExcelXlsx(String fileName) {
    return fileName.endsWith(Excel.XLSX.getName());
  }

  private Workbook multipartFileToWorkbook(MultipartFile multipartFile)
      throws IOException {
    if (isExcelXls(multipartFile.getOriginalFilename())) {
      return new HSSFWorkbook(multipartFile.getInputStream());
    } else {
      return new XSSFWorkbook(multipartFile.getInputStream());
    }
  }

}
