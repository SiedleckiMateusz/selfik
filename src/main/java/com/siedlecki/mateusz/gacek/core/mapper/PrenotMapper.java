package com.siedlecki.mateusz.gacek.core.mapper;

import com.siedlecki.mateusz.gacek.core.Constants;
import com.siedlecki.mateusz.gacek.core.model.PrenotProduct;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrenotMapper {

    public static Map<String,PrenotProduct> mapToPrenotProductMap(Sheet sheet) {
        Map<String,PrenotProduct> prenotMap = new HashMap<>();
        int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();
        for (int i = rows; i > Constants.PRENOT_ROW_INDEX; i--) {
            Row row = sheet.getRow(i);
            PrenotProduct product = mapToProduct(row);
            if (prenotMap.containsKey(product.getNumberId())){
                PrenotProduct productInMap = prenotMap.get(product.getNumberId());
                productInMap.addQtyBuffer(product.getQtyBuffer());
                productInMap.addQtySales(product.getQtySales());
            }else {
                prenotMap.put(product.getNumberId(),product);
            }
        }
        return prenotMap;
    }

    private static PrenotProduct mapToProduct(Row row) {
        int qtyBuffer = 0;
        int qtySales = 0;
        int qty = (int) row.getCell(15).getNumericCellValue();
        if (row.getCell(14).getStringCellValue().equals("Sales")) {
            qtySales+=qty;
        } else {
            qtyBuffer+=qty;
        }
        String id = String.valueOf((int) row.getCell(1).getNumericCellValue());

        while (id.length() < 8) {
            id = "0" + id;
        }

        return new PrenotProduct(id, qtySales,qtyBuffer);
    }

}
