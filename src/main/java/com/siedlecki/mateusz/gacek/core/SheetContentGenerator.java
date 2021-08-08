package com.siedlecki.mateusz.gacek.core;

import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;

import java.util.ArrayList;
import java.util.List;

public class SheetContentGenerator {

    public static String[] TO_PREPARE_COLUMNS = new String[]{
            "SPECSHOP","RANGE GR","ART NR","ART NAME","SLID","PRENOT ORDER[PQ]","L23 ORDER[PQ]"
    };

    public static String[] TO_ORDER_COLUMNS = new String[]{
            "ART NR","ART NAME","SLID","PRENOT ORDER[PQ]","L23 ORDER[PQ]"
    };

    public static String[] ALL_PARAM_COLUMNS = new String[]{
            "SPECSHOP","RANGE GR","ART NR","ART NAME","SLID","FCST","ASSQ","AVG SALES","PQ","AVAL STOCK",
            "SGF","PRENOT BUFFER","BUFFER & SGF","BUFFER & SGF [PQ]","VOL","PRENOT SALES","PRENOT SALES [PQ]",
            "ON SALES PLACES","FREE SPACE","L23 QTY","L23[PQ]","L23 [FULL PQ]","FREE SPACE AFTER ORD"
    };

    public static List<String[]> toPrepareSheetValues(List<IkeaProduct> products){
        List<String[]> result = new ArrayList<>();
        for (IkeaProduct product : products){
            result.add(new String[]{
                    product.getSpecshop(),
                    product.getRangeGroup(),
                    product.getNumberId(),
                    product.getName(),
                    product.getLocations().toString(),
                    String.format("%.2f", product.prenotSalesPQ()),
                    String.valueOf(product.l23OrderToFullPal())
            });
        }
        return result;
    }

    public static List<String[]> toOrderSheetValues(List<IkeaProduct> products){
        List<String[]> result = new ArrayList<>();
        for (IkeaProduct product : products){
            result.add(new String[]{
                    product.getNumberId(),
                    product.getName(),
                    product.getLocations().toString(),
                    String.format("%.2f", product.prenotSalesPQ()),
                    String.valueOf(product.l23OrderToFullPal())
            });
        }
        return result;
    }

    public static List<String[]> allParamSheetValues(List<IkeaProduct> products){
        List<String[]> result = new ArrayList<>();
        for (IkeaProduct product : products){
            result.add(new String[]{
                    product.getSpecshop(),
                    product.getRangeGroup(),
                    product.getNumberId(),
                    product.getName(),
                    product.getLocations().toString(),
                    product.getFcst().toString(),
                    product.getAssq().toString(),
                    String.format("%.2f", product.getAvgSales()),
                    product.getPalQty().toString(),
                    product.getAvailableStock().toString(),
                    product.getSgf().toString(),
                    String.valueOf(product.getPrenotBuffer()),
                    String.valueOf(product.bufferAndSgf()),
                    String.valueOf(product.bufferAndSgfPQ()),
                    String.format("%.2f", product.getVolume()),
                    String.valueOf(product.getPrenotSales()),
                    String.format("%.2f", product.prenotSalesPQ()),
                    String.valueOf(product.onSalePlaces()),
                    String.valueOf(product.freeSpace()),
                    String.valueOf(product.l23Order()),
                    String.format("%.2f", product.l23OrderPQ()),
                    String.valueOf(product.l23OrderToFullPal()),
                    String.valueOf(product.freeSpaceAfterOrder())
            });
        }
        return result;
    }


}
