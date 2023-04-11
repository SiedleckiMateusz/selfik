package com.siedlecki.mateusz.gacek.core.mapper;

import com.siedlecki.mateusz.gacek.core.Constants;
import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.core.model.Iv020Column;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Iv020Mapper {

    private final Map<String,Integer> reservedMap = new HashMap<>();

    public Iv020Mapper(Sheet sheet){
        mapToReservedMap(sheet);
    }

    private void mapToReservedMap(Sheet sheet){

        int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();
        for (int i = Constants.SLM0003_ROW_INDEX +1; i<rows; i++){
            Row row = sheet.getRow(i);
            String productNuber = row.getCell(Iv020Column.NUMBER.getIndex()).getStringCellValue();
            int reservedQty = (int) row.getCell(Iv020Column.RESERVED_QTY.getIndex()).getNumericCellValue();
            reservedMap.put(productNuber, reservedQty);
        }
    }

    public void procesResevationIkeaProducts(Map<String, IkeaProduct> ikeaProductMap) {
        for (String productNumber : reservedMap.keySet()) {
            if (ikeaProductMap.containsKey(productNumber)) {
                ikeaProductMap.get(productNumber).addReserved(reservedMap.get(productNumber));
            }
        }
    }
}
