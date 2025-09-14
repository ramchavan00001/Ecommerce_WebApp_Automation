package ramchavantestautomation.Utils;
//package com.ramchavantestautomation.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtil {

    public static Object[][] readExcelData(String filePath, String sheetName) {
        Object[][] data = null;
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            int rows = sheet.getPhysicalNumberOfRows();
            if (rows <= 1) {
                return new Object[0][0];
            }

            int cols = sheet.getRow(0).getLastCellNum();
            DataFormatter formatter = new DataFormatter();
            data = new Object[rows - 1][cols];

            for (int i = 1; i < rows; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < cols; j++) {
                    Cell cell = (row == null) ? null : row.getCell(j);
                    String value = formatter.formatCellValue(cell);
                    data[i - 1][j] = value;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
        return data;
    }
}
