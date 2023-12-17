package com.example.demo;

import com.example.demo.Transaction;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import static org.apache.poi.hssf.model.InternalSheet.createSheet;

public class ExcelReader {
    public HashMap<Integer, List<Object>> read(String filename) throws IOException {
        Workbook workbook = loadWorkbook(filename);
        HashMap<Integer, List<Object>> excelData = new HashMap<Integer, List<Object>>();
        var sheetIterator = workbook.sheetIterator();
        while (((Iterator<?>) sheetIterator).hasNext()) {
            Sheet sheet = sheetIterator.next();
            excelData = processSheet(sheet);
        }
        return excelData;
    }
    public void write(String filename, Transaction[] data) throws IOException {
        var workbook = new XSSFWorkbook();
        var sheet = createSheet(workbook);
        createHeader(workbook, sheet);
        createCells(workbook, sheet, data);
        try (var outputStream = new FileOutputStream(filename)) {
            workbook.write(outputStream);
        }
        workbook.close();
    }
    private Workbook loadWorkbook(String filename) throws IOException {
        var extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        var file = new FileInputStream(new File(filename));
        switch (extension) {
            case "xls":
                return new HSSFWorkbook(file);
            case "xlsx":
                return new XSSFWorkbook(file);
            default:
                throw new RuntimeException("Unknown Excel file extension: " + extension);
        }
    }
    private HashMap<Integer, List<Object>> processSheet(Sheet sheet) {
        var data = new HashMap<Integer, List<Object>>();
        var iterator = sheet.rowIterator();
        for (var rowIndex = 0; iterator.hasNext(); rowIndex++) {
            var row = iterator.next();
            processRow(data, rowIndex, row);
        }
        return data;
    }
    private void processRow(HashMap<Integer, List<Object>> data, int rowIndex, Row row) {
        data.put(rowIndex, new ArrayList<>());
        for (var cell : row) {
            processCell(cell, data.get(rowIndex));
        }
    }
    private void processCell(Cell cell, List<Object> dataRow) {
        switch (cell.getCellType()) {
            case STRING:
                dataRow.add(cell.getStringCellValue());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    dataRow.add(cell.getLocalDateTimeCellValue());
                } else {
                    dataRow.add(NumberToTextConverter.toText(cell.getNumericCellValue()));
                }
                break;
            case BOOLEAN:
                dataRow.add(cell.getBooleanCellValue());
                break;
            case FORMULA:
                dataRow.add(cell.getCellFormula());
                break;
            default:
                dataRow.add(" ");
        }
    }
    private Sheet createSheet(XSSFWorkbook workbook) {
        var sheet = workbook.createSheet("Фальтрация по категории и дате");
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 4000);
        return sheet;
    }

    private void createHeader(XSSFWorkbook workbook, Sheet sheet) {
        var header = sheet.createRow(0);

        var headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        var font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        headerStyle.setFont(font);

        var headerCell = header.createCell(0);
        headerCell.setCellValue("Агент");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Дата");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Описание");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Категория");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Тег");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(5);
        headerCell.setCellValue("Сумма");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(6);
        headerCell.setCellValue("Id");
        headerCell.setCellStyle(headerStyle);
    }

    private void createCells(XSSFWorkbook workbook, Sheet sheet, Transaction... data) {
        var style = workbook.createCellStyle();
        style.setWrapText(true);
        var createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd.mm.yyyy"));
        sheet.setDefaultColumnStyle(1, style);

        int i = 0;
        for (Transaction rec: data) {
            var row = sheet.createRow(i + 1);

            var cell = row.createCell(0);
            cell.setCellValue(rec.getAgent());

            cell = row.createCell(1);
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd hh:mm:ss a zzz");
            cell.setCellValue(formatForDateNow.format(rec.getDate()));

            cell = row.createCell(2);
            cell.setCellValue(rec.getDescription());

            cell = row.createCell(3);
            cell.setCellValue(rec.getCategory());

            cell = row.createCell(4);
            cell.setCellValue(rec.getTag());

            cell = row.createCell(5);
            cell.setCellValue(Float.toString(rec.getAmount()));

            cell = row.createCell(6);
            cell.setCellValue(Float.toString(rec.getId()));
            i++;
        }
    }
}
