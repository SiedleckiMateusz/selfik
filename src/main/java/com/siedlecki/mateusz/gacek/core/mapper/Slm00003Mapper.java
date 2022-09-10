package com.siedlecki.mateusz.gacek.core.mapper;

import com.siedlecki.mateusz.gacek.core.Constants;
import com.siedlecki.mateusz.gacek.core.ProductsContainer;
import com.siedlecki.mateusz.gacek.core.model.*;
import org.apache.commons.compress.utils.Sets;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Slm00003Mapper {

    public static void mapToProductsMap(Sheet sheet, ProductsContainer container) throws IOException {
        List<LocationHelper> locationOrder = sortSheetByLocation(sheet);

        Map<String,IkeaProduct> productMap = new HashMap<>();
        Map<String,LocationsWithProducts> locations = new HashMap<>();

        for (LocationHelper loc : locationOrder){
            Row row = sheet.getRow(loc.getIndex());
            if (!rowsIsIgnored(row)){
                String productId = row.getCell(5).getStringCellValue();
                String locationName = row.getCell(7).getStringCellValue();
                if (locations.containsKey(locationName)){
                    locations.get(locationName).getArtilceIds().add(productId);
                }else {
                    locations.put(locationName,new LocationsWithProducts(locationName, Sets.newHashSet(productId)));
                }
                if (productMap.containsKey(productId)){
                    IkeaProduct product = productMap.get(productId);
                    product.getLocations().add(
                            buildLocation(row)
                    );
                    product.addAssq((int) row.getCell(16).getNumericCellValue());
                }else {
                    productMap.put(productId,mapToProduct(row));
                }
            }
        }
        container.setLocations(new HashSet<>(locations.values()));
        container.setIkeaProductMap(productMap);
    }

    private static Location buildLocation(Row row) {
        return Location.builder()
                .main(row.getCell(8).getStringCellValue().equals("3"))
                .rangeGroup(row.getCell(2).getStringCellValue())
                .specshop(setSpecshop(row.getCell(1).getStringCellValue()))
                .name(row.getCell(7).getStringCellValue())
                .build();
    }

    private static List<LocationHelper> sortSheetByLocation(Sheet sheet) {
        List<LocationHelper> locations = new ArrayList<>();

        int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();
        for (int i = Constants.SLM0003_ROW_INDEX +1; i<rows; i++){
            locations.add(new LocationHelper(i,sheet.getRow(i).getCell(7).getStringCellValue()));
        }
        return locations.stream()
                .sorted(Comparator.comparing(LocationHelper::getLocation))
                .collect(Collectors.toList());
    }

    private static boolean rowsIsIgnored(Row row) {
        for (Column column : Constants.SLM0003_IGNORED_VALUES){
            String cellValue = row.getCell(column.getIndex()).getStringCellValue();
            if (cellValue.contains(column.getName())){
                return true;
            }
        }
        return false;
    }

    private static IkeaProduct mapToProduct(Row row) throws IOException {
        Set<Location> locations = new HashSet<>();
        locations.add(
                buildLocation(row)
        );

        return IkeaProduct.builder()
                .id(row.getCell(5).getStringCellValue())
                .name(row.getCell(6).getStringCellValue())
                .locations(locations)
                .fcst((int)row.getCell(14).getNumericCellValue())
                .assq((int)row.getCell(16).getNumericCellValue())
                .avgSales(row.getCell(42).getNumericCellValue())
                .palQty((int)row.getCell(31).getNumericCellValue())
                .availableStock((int)row.getCell(43).getNumericCellValue())
                .sgf((int)row.getCell(44).getNumericCellValue())
                .volume(mapToDoubleNumber(row.getCell(46)))
                .build();
    }

    private static String setSpecshop(String specshop) {
        if (specshop.toLowerCase().startsWith("odd")) return "NP";
        if (specshop.toLowerCase().startsWith("even")) return "P";
        return "-";
    }

    private static Double mapToDoubleNumber(Cell cell) throws IOException {
        CellType cellType = cell.getCellType();
        switch (cellType){
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                String stringCellValue = cell.getStringCellValue();
                return Double.parseDouble(stringCellValue);
        }
        throw new IOException("CellType is not numeric and not string value");
    }
}
