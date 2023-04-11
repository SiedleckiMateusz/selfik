package com.siedlecki.mateusz.gacek.core.reader;

import com.siedlecki.mateusz.gacek.core.model.PrenotColumn;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PrenotReader extends SheetReader{

    private final List<PrenotColumn> columns;
    private final List<PrenotColumn> founded = new ArrayList<>();

    public PrenotReader(String sheetName, int numOfColumnRow) {
        super(sheetName, numOfColumnRow);
        columns = Arrays.asList(PrenotColumn.values());
    }

    @Override
    protected void setIndex(Cell stringCell) {
        String name = stringCell.getStringCellValue();
        for (PrenotColumn column: columns){
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
                .map(PrenotColumn::getName)
                .collect(Collectors.toList());
    }
}
