package com.siedlecki.mateusz.gacek.core.reader;


import com.siedlecki.mateusz.gacek.core.model.Column;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@AllArgsConstructor
public class SheetReader {
    private final String sheetName;
    private final int numOfColumnRow;
    private final Set<Column> columnSet;

    public Sheet getSheetFromFile(MultipartFile multipartFile) throws IOException {
        Sheet sheet = createWorkbook(multipartFile).getSheet(sheetName);
        if (sheet==null){
            throw new IOException("Plik "+ multipartFile.getName() +" nie posiada arkusza o nazwie "+sheetName);
        }
        checkingColumn(sheet,columnSet,multipartFile.getName());
        return sheet;
    }

    private boolean checkingColumn(Sheet sheet, Set<Column> columnsInFileSet, String fileName) throws IOException {
        Row row = sheet.getRow(numOfColumnRow);
        for (Column c : columnsInFileSet){
            if (!isCorrectColumn(row,c)){
                throw new IOException("Niepoprawna kolumna: index "+c.getIndex()+", nazwa "+c.getName()+" w pliku "+fileName);
            }
        }
        return true;
    }

    private boolean isCorrectColumn(Row row, Column column){
        return row.getCell(column.getIndex()).getStringCellValue().trim().equals(column.getName().toUpperCase());
    }

    private Workbook createWorkbook(MultipartFile file) throws IOException {
        try(InputStream inputStream = file.getInputStream()){
            String name = file.getOriginalFilename();
            if (name.endsWith(".xlsx")) {
                return new XSSFWorkbook(inputStream);
            }
            if (name.endsWith(".xls")) {
                return new HSSFWorkbook(inputStream);
            }
            throw new IOException("Plik "+ name +" nie jest w formacie .xls lub .xlsx");
        }catch (Exception e){
            throw new IOException(e.getMessage());
        }
    }
}
