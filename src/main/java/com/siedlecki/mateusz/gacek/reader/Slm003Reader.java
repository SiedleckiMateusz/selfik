package com.siedlecki.mateusz.gacek.reader;

import com.siedlecki.mateusz.gacek.model.Slm003Column;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Slm003Reader extends SheetReader {

    private final List<Slm003Column> columns;
    private final List<Slm003Column> founded = new ArrayList<>();

    public Slm003Reader(String sheetName, int numOfColumnRow) {
        super(sheetName, numOfColumnRow);
        columns = Arrays.asList(Slm003Column.values());
    }

    @Override
    protected void setIndex(Cell stringCell) {
        String cellValue = stringCell.getStringCellValue();
        for (Slm003Column column : columns) {
            if (column.getSettings().name().equals(cellValue)) {
                column.setIndex(stringCell.getColumnIndex());
                founded.add(column);
                break;
            }
        }
    }

    @Override
    protected List<String> getEmptyColumnNames() {
        if (founded.size() == columns.size()) {
            return Collections.emptyList();
        }
        List<String> foundedNames = founded.stream()
                .map(slm003Column -> slm003Column.getSettings().name())
                .collect(Collectors.toList());
        return columns.stream()
                .map(slm003Column -> slm003Column.getSettings().name())
                .filter(c -> !foundedNames.contains(c))
                .collect(Collectors.toList());
    }

}
