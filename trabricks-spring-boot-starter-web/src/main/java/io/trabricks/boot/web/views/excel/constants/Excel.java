package io.trabricks.boot.web.views.excel.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The enum Excel.
 */
@Getter
@RequiredArgsConstructor
public enum Excel {

  /**
   * File name excel.
   */
  FILE_NAME("fileName"),
  /**
   * Head excel.
   */
  HEAD("head"),
  /**
   * Body excel.
   */
  BODY("body"),
  /**
   * Xls excel.
   */
  XLS("xls"),
  /**
   * Xlsx excel.
   */
  XLSX("xlsx"),
  /**
   * Xlsx stream excel.
   */
  XLSX_STREAM("xlsx-stream");

  private final String name;

}
