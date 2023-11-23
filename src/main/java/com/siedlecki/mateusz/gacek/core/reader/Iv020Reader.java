package com.siedlecki.mateusz.gacek.core.reader;

import com.siedlecki.mateusz.gacek.core.model.Iv020Column;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Iv020Reader extends SheetReader {

    private final List<Iv020Column> columns;
    private final List<Iv020Column> founded = new ArrayList<>();

    public Iv020Reader(String sheetName, int numOfColumnRow) {
        super(sheetName, numOfColumnRow);
        columns = Arrays.asList(Iv020Column.values());
    }

    @Override
    protected void setIndex(Cell stringCell) {
        String name = stringCell.getStringCellValue();
        for (Iv020Column column : columns) {
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
                .map(Iv020Column::getName)
                .collect(Collectors.toList());
    }

}
