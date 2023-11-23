package com.siedlecki.mateusz.gacek.core.reader;

import com.siedlecki.mateusz.gacek.core.model.Slm003Column;
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
        String name = stringCell.getStringCellValue();
        for (Slm003Column column : columns) {
            if (column.getName().equals(name)) {
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
        return columns.stream()
                .filter(c -> !founded.contains(c))
                .map(Slm003Column::getName)
                .collect(Collectors.toList());
    }

}
