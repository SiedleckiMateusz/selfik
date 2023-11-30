package com.siedlecki.mateusz.gacek.reader;

import com.siedlecki.mateusz.gacek.model.Iv020Column;
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
            if (column.getSettings().getValue().equals(name)) {
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
        List<String> foundedNames = founded.stream().map(iv020Column -> iv020Column.getSettings().name()).collect(Collectors.toList());

        return columns.stream()
                .map(iv020Column -> iv020Column.getSettings().name())
                .filter(c -> !foundedNames.contains(c))
                .collect(Collectors.toList());
    }

}
