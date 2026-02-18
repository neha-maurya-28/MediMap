package com.selenium.utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ExcelUtils {

    private Workbook workbook;

    public ExcelUtils(String fileName) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testdata/" + fileName);
            if (inputStream == null) {
                throw new RuntimeException("Excel file not found: " + fileName);
            }
            workbook = new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load Excel file: " + fileName, e);
        }
    }

    public ExcelUtils(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            workbook = new XSSFWorkbook(fis);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load Excel file: " + file.getAbsolutePath(), e);
        }
    }

    public String getCell(String sheetName, int rowNum, int colNum) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) return null;

        Row row = sheet.getRow(rowNum);
        if (row == null) return null;
        Cell cell = row.getCell(colNum);
        if (cell == null) return null;
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue();
    }

    public int getRowCount(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) return 0;

        int last = sheet.getLastRowNum();
        int count = 0;
        for (int r = 1; r <= last+1; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;

            boolean hasData = false;

            for (Cell cell : row) {
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    String v = cell.getStringCellValue();
                    if (v != null && !v.trim().isEmpty()) {
                        hasData = true;
                        break;
                    }
                }
            }
            if (hasData) count++;
        }
        return count;
    }
}