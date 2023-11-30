package com.siedlecki.mateusz.gacek.mapper;

import com.siedlecki.mateusz.gacek.settings.Utils;
import com.siedlecki.mateusz.gacek.model.PrenotProduct;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.Map;

import static com.siedlecki.mateusz.gacek.model.PrenotColumn.*;

public class PrenotMapper {

    public static Map<String,PrenotProduct> mapToPrenotProductMap(Sheet sheet) {
        Map<String,PrenotProduct> prenotMap = new HashMap<>();
        int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();
        for (int i = rows; i > Utils.PRENOT_ROW_INDEX; i--) {
            Row row = sheet.getRow(i);
            PrenotProduct product = mapToProduct(row);
            if (prenotMap.containsKey(product.getId())){
                PrenotProduct productInMap = prenotMap.get(product.getId());
                productInMap.addQtyBuffer(product.getQtyBuffer());
                productInMap.addQtySales(product.getQtySales());
            }else {
                prenotMap.put(product.getId(),product);
            }
        }
        return prenotMap;
    }

    private static PrenotProduct mapToProduct(Row row) {
        int qtyBuffer = 0;
        int qtySales = 0;
        int qty = (int) row.getCell(ILOSC.getIndex()).getNumericCellValue();
        String direction = row.getCell(DO.getIndex()).getStringCellValue();
        if (direction.equals("Sales")) {
            qtySales+=qty;
        } else if (direction.equals("Buffer")){
            qtyBuffer+=qty;
        }
        StringBuilder id = new StringBuilder(String.valueOf((int) row.getCell(NUMER.getIndex()).getNumericCellValue()));

        while (id.length() < 8) {
            id.insert(0, "0");
        }

        return new PrenotProduct(id.toString(), qtySales,qtyBuffer);
    }

}
