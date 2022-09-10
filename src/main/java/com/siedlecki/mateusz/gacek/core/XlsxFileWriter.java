package com.siedlecki.mateusz.gacek.core;

import lombok.Getter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

@Getter
public class XlsxFileWriter {

    private final XSSFWorkbook workbook;
    private final String fileName;

    public XlsxFileWriter(String fileName){
        this.fileName = fileName;
        this.workbook = new XSSFWorkbook();
    }

    public XlsxFileWriter addSheet(List<String[]> products,String[] columnNames, String sheetName){
        XSSFSheet sheet = workbook.createSheet(sheetName);
        XSSFRow headerRow = sheet.createRow(0);

        fillRow(headerRow,columnNames);
        createContentRows(sheet,products);

        return this;
    }

    private void createContentRows(XSSFSheet sheet,List<String[]> products) {
        for (int i=0;i<products.size();i++){
            XSSFRow row = sheet.createRow(i+1);
            fillRow(row,products.get(i));
        }
    }

    private void fillRow(XSSFRow row, String[] values) {
        for (int i = 0;i< values.length;i++){
            row.createCell(i).setCellValue(values[i]);
        }
    }



}
