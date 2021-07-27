package com.siedlecki.mateusz.gacek.core.mapper;

import com.siedlecki.mateusz.gacek.core.Constants;
import com.siedlecki.mateusz.gacek.core.model.opq.PickingProduct;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpqMapper {

    public static List<PickingProduct> mapToPickingProduct(Sheet sheet) {
        Map<String, PickingProduct> productMap = new HashMap<>();
        int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();
        for (int i = rows; i > Constants.OPQ_ROW_INDEX; i--) {
            Row row = sheet.getRow(i);
            PickingProduct product = mapToProduct(row);

            //todo: finish this function
        }

        return new ArrayList<>(productMap.values());
    }

    private static PickingProduct mapToProduct(Row row) {
        return null;
    }
}
