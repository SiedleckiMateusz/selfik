package com.siedlecki.mateusz.gacek.reader;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@Slf4j
@AllArgsConstructor
public abstract class SheetReader {
    private final String sheetName;
    private final int numOfColumnRow;

    public Sheet getCorrectSheetFromFile(MultipartFile multipartFile) throws IOException {
        Sheet sheet = createWorkbook(multipartFile).getSheet(sheetName);
        if (sheet == null) {
            String message = "Plik " + multipartFile.getName() + " nie posiada arkusza o nazwie " + sheetName;
            log.error(message);
            throw new IOException(message);
        }
        searchingColumn(sheet.getRow(numOfColumnRow));
        return sheet;
    }

    private void searchingColumn(Row row) {
        log.info("Searching column");
        Iterator<Cell> cellIterator = row.cellIterator();
        do {
            Cell next = cellIterator.next();
            setIndex(next);
        } while (cellIterator.hasNext());
        List<String> emptyIndexColumnNames = getEmptyColumnNames();
        if (!emptyIndexColumnNames.isEmpty()) {
            String message = "Nie znaleziono wszystkich kolumn w podanym pliku. Brakujące kolumny" + emptyIndexColumnNames;
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    protected abstract void setIndex(Cell stringCell);

    protected abstract List<String> getEmptyColumnNames();

    private Workbook createWorkbook(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            String name = file.getOriginalFilename();
            if (name != null) {
                if (name.endsWith(".xlsx")) {
                    return new XSSFWorkbook(inputStream);
                }
                if (name.endsWith(".xls")) {
                    return new HSSFWorkbook(inputStream);
                }
            }
            String message = "Plik " + name + " nie jest w formacie .xls lub .xlsx";
            log.error(message);
            throw new IOException(message);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IOException(e.getMessage());
        }
    }
}
