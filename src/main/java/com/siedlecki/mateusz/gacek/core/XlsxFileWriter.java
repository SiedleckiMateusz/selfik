package com.siedlecki.mateusz.gacek.core;

import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import lombok.Getter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

@Getter
public class XlsxFileWriter {

    private final XSSFWorkbook workbook;
    private final String fileName;

    public XlsxFileWriter(String fileName) throws IOException {
        this.fileName = fileName;
        this.workbook = new XSSFWorkbook();
    }

    public XlsxFileWriter addSheet(List<IkeaProduct> products, String sheetName){
        XSSFSheet sheet = workbook.createSheet(sheetName);
        XSSFRow headerRow = sheet.createRow(0);

        createHeaderRow(headerRow, Constants.EXCEL_COLUMNS_NAMES);
        createContentRows(sheet,products);

        return this;
    }

    private void createContentRows(XSSFSheet sheet,List<IkeaProduct> products) {
        for (int i=0;i<products.size();i++){
            XSSFRow row = sheet.createRow(i+1);
            createRow(row,products.get(i));
        }
    }

    private void createRow(XSSFRow row, IkeaProduct ikeaProductToWrite) {
        row.createCell(0).setCellValue(ikeaProductToWrite.getSpecshop());
        row.createCell(1).setCellValue(ikeaProductToWrite.getRangeGroup());
        row.createCell(2).setCellValue(ikeaProductToWrite.getNumberId());
        row.createCell(3).setCellValue(ikeaProductToWrite.getName());
        row.createCell(4).setCellValue(ikeaProductToWrite.getLocations().toString());
        row.createCell(5).setCellValue(ikeaProductToWrite.getAssq());
        row.createCell(6).setCellValue(ikeaProductToWrite.getPalQty());
        row.createCell(7).setCellValue(ikeaProductToWrite.getAvailableStock());
        row.createCell(8).setCellValue(ikeaProductToWrite.onSalePlaces());
        row.createCell(9).setCellValue(ikeaProductToWrite.freeSpaceBeforePrenot());
        row.createCell(10).setCellValue(String.format("%.2f", ikeaProductToWrite.freeSpaceBeforePrenotPQ()));
        row.createCell(11).setCellValue(String.format("%.2f", ikeaProductToWrite.bufferAndSgfPQ()));
        row.createCell(12).setCellValue(String.format("%.2f", ikeaProductToWrite.prenotSalesPQ()));
        row.createCell(13).setCellValue(ikeaProductToWrite.toL23OrderPQ());
        row.createCell(14).setCellValue(ikeaProductToWrite.freeSpaceAfterOrder());
    }

    private void createHeaderRow(XSSFRow row,String[] names){
        for (int i = 0; i<names.length;i++){
            row.createCell(i).setCellValue(names[i]);
        }
    }



}
