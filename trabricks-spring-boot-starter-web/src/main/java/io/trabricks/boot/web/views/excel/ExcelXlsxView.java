package io.trabricks.boot.web.views.excel;

import io.trabricks.boot.web.views.excel.components.ExcelWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

/**
 * The type Excel xlsx view.
 */
public class ExcelXlsxView extends AbstractXlsxView {

  @Override
  protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    new ExcelWriter(workbook, model, request, response).create();
  }
}
