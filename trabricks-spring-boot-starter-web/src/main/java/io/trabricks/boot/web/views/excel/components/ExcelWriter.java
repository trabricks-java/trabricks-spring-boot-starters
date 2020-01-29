package io.trabricks.boot.web.views.excel.components;

import eu.bitwalker.useragentutils.UserAgent;
import io.trabricks.boot.web.utils.FileNameEncoder;
import io.trabricks.boot.web.views.excel.constants.Excel;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {

  private Workbook workbook;
  private Map<String, Object> model;
  private HttpServletRequest request;
  private HttpServletResponse response;

  public ExcelWriter(Workbook workbook, Map<String, Object> model, HttpServletRequest request,
      HttpServletResponse response) {
    this.workbook = workbook;
    this.model = model;
    this.request = request;
    this.response = response;
  }

  public void create() {
    applyFileNameForRequest(mapToFileName());

    applyContentTypeForRequest();

    Sheet sheet = workbook.createSheet();

    createHead(sheet, mapToHeadList());

    createBody(sheet, mapToBodyList());
  }

  private String mapToFileName() {
    return (String) model.get(Excel.FILE_NAME.getName());
  }

  private List<String> mapToHeadList() {
    return (List<String>) model.get(Excel.HEAD.getName());
  }

  private List<List<String>> mapToBodyList() {
    return (List<List<String>>) model.get(Excel.BODY.getName());
  }

  private void applyFileNameForRequest(String fileName) {
    UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    String encodedFileName = FileNameEncoder.encode(userAgent.getBrowser().getGroup(), fileName);
    response.setHeader("Content-Disposition",
        "attachment; filename=\"" + appendFileExtension(encodedFileName) + "\"");
  }

  private String appendFileExtension(String fileName) {
    if (workbook instanceof XSSFWorkbook || workbook instanceof SXSSFWorkbook) {
      fileName += ".xlsx";
    }
    if (workbook instanceof HSSFWorkbook) {
      fileName += ".xls";
    }

    return fileName;
  }

  private void applyContentTypeForRequest() {
    if (workbook instanceof XSSFWorkbook || workbook instanceof SXSSFWorkbook) {
      response.setHeader("Content-Type",
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
    if (workbook instanceof HSSFWorkbook) {
      response.setHeader("Content-Type", "application/vnd.ms-excel");
    }
  }

  private void createHead(Sheet sheet, List<String> headList) {
    createRow(sheet, headList, 0);
  }

  private void createBody(Sheet sheet, List<List<String>> bodyList) {
    int rowSize = bodyList.size();
    for (int i = 0; i < rowSize; i++) {
      createRow(sheet, bodyList.get(i), i + 1);
    }
  }

  private void createRow(Sheet sheet, List<String> cellList, int rowNum) {
    int size = cellList.size();
    Row row = sheet.createRow(rowNum);

    for (int i = 0; i < size; i++) {
      row.createCell(i).setCellValue(cellList.get(i));
    }
  }

}