package com.siedlecki.mateusz.gacek.core.mapper;

import com.siedlecki.mateusz.gacek.core.Constants;
import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.core.model.opq.Pick;
import com.siedlecki.mateusz.gacek.core.model.opq.PickingProduct;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class OpqMapper {

    public static Map<String, PickingProduct> mapToPickingProduct(Sheet sheet, Map<String, IkeaProduct> ikeaProductMap) {
        Map<String, PickingProduct> pickingProductMap = new HashMap<>();
        int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();
        for (int i = rows; i > Constants.OPQ_ROW_INDEX; i--) {
            Row row = sheet.getRow(i);
            if (!ignoreRow(row)) {
                String numberId = row.getCell(2).getStringCellValue();
                if (ikeaProductMap.containsKey(numberId)) {
                    if (pickingProductMap.containsKey(numberId)) {
                        pickingProductMap.get(numberId).addPick(mapToPick(row));
                    } else {
                        pickingProductMap.put(numberId, mapToProduct(row));
                    }
                }
            }
        }
        return pickingProductMap;
    }

    private static boolean ignoreRow(Row row) {
        return row.getCell(19).getStringCellValue().trim().isEmpty() ||
               row.getCell(20).getStringCellValue().trim().isEmpty() ||
               row.getCell(27).getStringCellValue().trim().isEmpty();
    }

    private static PickingProduct mapToProduct(Row row) {
        Cell artNoCell = row.getCell(2);
        Cell pickAreaCell = row.getCell(14);

        return new PickingProduct(artNoCell.getStringCellValue()
                , pickAreaCell.getStringCellValue()
                , mapToPick(row));

    }

    private static Pick mapToPick(Row row) {
        Cell pickQtyCell = row.getCell(7);
        Cell cutOffDateCell = row.getCell(19);
        Cell cutOffTimeCell = row.getCell(20);
        Cell deliveryMethodCell = row.getCell(27);

        LocalTime cutOffTime = null;
        LocalDate cutOffDate = null;
        try{
            cutOffDate = LocalDate.parse(cutOffDateCell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }catch (DateTimeParseException e){
            System.err.println("Nie można sparsować daty: "+cutOffDateCell.getStringCellValue());
        }

        try{
            cutOffTime = LocalTime.parse(cutOffTimeCell.getStringCellValue(), DateTimeFormatter.ofPattern("HH:mm"));
        }catch (DateTimeParseException e) {
            System.err.println(e.getMessage());
        }


        return Pick.builder()
                .pickQty((int) pickQtyCell.getNumericCellValue())
                .cutOffDate(cutOffDate)
                .cutOffTime(cutOffTime)
                .deliveryMethod(deliveryMethodCell.getStringCellValue())
                .build();
    }
}
