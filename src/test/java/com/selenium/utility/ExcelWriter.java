package com.selenium.utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

public class ExcelWriter {

    private final String filePath;
    private Workbook sessionWb;

    public ExcelWriter(String filePath) {
        this.filePath = filePath;
    }

    // ------------------ LIST WRITERS (kept) ------------------
    public void writeHospitalList(String sheetName, List<String> list) {
        writeSimpleList(sheetName, "Hospital Name", list);
    }

    public void writeCityList(String sheetName, List<String> list) {
        writeSimpleList(sheetName, "City Name", list);
    }

    // ------------------ CORE SIMPLE LIST WRITER ------------------
    private void writeSimpleList(String sheetName, String headerName, List<String> values) {
        Workbook wb = loadWorkbook(filePath);
        Sheet sheet = getOrCreateSheet(wb, sheetName);

        clearSheetKeepHeader(sheet);

        Row header = getOrCreateRow(sheet, 0);
        if (header.getCell(0) == null) header.createCell(0).setCellValue("Serial No.");
        if (header.getCell(1) == null) header.createCell(1).setCellValue(headerName);

        int row = 1;
        int serial = 1;

        for (String val : values) {
            Row r = sheet.createRow(row++);
            r.createCell(0).setCellValue(serial++);
            r.createCell(1).setCellValue(val);
        }

        saveWorkbook(wb, filePath);
        close(wb);
    }

    // ------------------ BASIC HELPERS ------------------
    private Workbook loadWorkbook(String path) {
        try (FileInputStream fis = new FileInputStream(path)) {
            return new XSSFWorkbook(fis);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveWorkbook(Workbook wb, String path) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            wb.write(fos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Sheet getOrCreateSheet(Workbook wb, String name) {
        Sheet s = wb.getSheet(name);
        return (s != null) ? s : wb.createSheet(name);
    }

    private Row getOrCreateRow(Sheet sheet, int index) {
        Row r = sheet.getRow(index);
        return (r != null) ? r : sheet.createRow(index);
    }

    private void clearSheetKeepHeader(Sheet sheet) {
        for (int r = sheet.getLastRowNum(); r > 0; r--) {
            Row row = sheet.getRow(r);
            if (row != null) sheet.removeRow(row);
        }
    }

    private void close(Workbook wb) {
        try { wb.close(); } catch (Exception ignored) {}
    }
}