package io.trabricks.boot.web.views.excel.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Excel {

  FILE_NAME("fileName"),
  HEAD("head"),
  BODY("body"),
  XLS("xls"),
  XLSX("xlsx"),
  XLSX_STREAM("xlsx-stream");

  private final String name;

}
