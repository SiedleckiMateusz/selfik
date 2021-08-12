package com.siedlecki.mateusz.gacek.core.mapper;

import com.siedlecki.mateusz.gacek.core.Constants;
import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.core.model.opq.Pick;
import com.siedlecki.mateusz.gacek.core.model.opq.PickingInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class OpqMapper {

    public static Map<String, IkeaProduct> processPickingProduct(Sheet sheet, Map<String, IkeaProduct> ikeaProductMap,int daysToPick) {
        int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();
        for (int i = rows; i > Constants.OPQ_ROW_INDEX; i--) {
            Row row = sheet.getRow(i);
            if (!ignoreRow(row)) {
                String numberId = row.getCell(2).getStringCellValue().trim();
                if (ikeaProductMap.containsKey(numberId)) {
                    IkeaProduct ikeaProduct = ikeaProductMap.get(numberId);
                    if (ikeaProduct.getPickingInfo()==null) {
                        ikeaProduct.setPickingInfo(new PickingInfo(mapToPick(row), daysToPick));
                    } else {
                        ikeaProduct.getPickingInfo().addPick(mapToPick(row));
                    }
                }
            }
        }
        return ikeaProductMap;
    }

    private static boolean ignoreRow(Row row) {
        return row.getCell(2).getStringCellValue().trim().isEmpty();
    }

    private static Pick mapToPick(Row row) {
        Cell pickQtyCell = row.getCell(7);
        Cell cutOffDateCell = row.getCell(19);
        Cell cutOffTimeCell = row.getCell(20);

        int pickQty = (int) pickQtyCell.getNumericCellValue();

        LocalDate cutOffDate = null;
        String cutOffDateString = cutOffDateCell.getStringCellValue().trim();

        if (!cutOffDateString.isEmpty()){
            try{
                cutOffDate = LocalDate.parse(cutOffDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }catch (DateTimeParseException e){
                System.err.println("Nie można sparsować daty: "+ cutOffDateString);
            }
        }

        LocalTime cutOffTime = null;
        String cutOffTimeString = cutOffTimeCell.getStringCellValue().trim();
        if (!cutOffTimeString.isEmpty()){
            try{
                cutOffTime = LocalTime.parse(cutOffTimeString, DateTimeFormatter.ofPattern("HH:mm"));
            }catch (DateTimeParseException e){
                System.err.println("Nie można sparsować godziny: "+ cutOffDateString);
            }
        }

        return new Pick(pickQty,cutOffDate,cutOffTime);
    }
}
