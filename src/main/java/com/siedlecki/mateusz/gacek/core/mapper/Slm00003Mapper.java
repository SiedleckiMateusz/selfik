package com.siedlecki.mateusz.gacek.core.mapper;

import com.siedlecki.mateusz.gacek.core.Constants;
import com.siedlecki.mateusz.gacek.core.model.*;
import lombok.Getter;
import org.apache.commons.compress.utils.Sets;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.siedlecki.mateusz.gacek.core.model.Slm003Column.*;

@Getter
public class Slm00003Mapper {

    private final Map<String,IkeaProduct> productMap = new HashMap<>();
    private Set<LocationsWithProducts> locations = new HashSet<>();

    public Slm00003Mapper(Sheet sheet) throws IOException {
        mapToProductsMap(sheet);
    }

    private void mapToProductsMap(Sheet sheet) throws IOException {
        List<LocationHelper> locationOrder = sortSheetByLocation(sheet);
        Map<String,LocationsWithProducts> locationsMap = new HashMap<>();

        for (LocationHelper loc : locationOrder){
            Row row = sheet.getRow(loc.getIndex());
            if (!rowsIsIgnored(row)){
                String productId = row.getCell(NUMBER.getIndex()).getStringCellValue();
                String locationName = row.getCell(SLID.getIndex()).getStringCellValue();
                if (locationsMap.containsKey(locationName)){
                    locationsMap.get(locationName).getArtilceIds().add(productId);
                }else {
                    locationsMap.put(locationName,new LocationsWithProducts(locationName, Sets.newHashSet(productId)));
                }
                if (productMap.containsKey(productId)){
                    IkeaProduct product = productMap.get(productId);
                    product.getLocations().add(
                            buildLocation(row)
                    );
                    product.addAssq((int) row.getCell(SSQ.getIndex()).getNumericCellValue());
                }else {
                    productMap.put(productId,mapToProduct(row));
                }
            }
        }
        this.locations = new HashSet<>(locationsMap.values());

    }

    private Location buildLocation(Row row) {
        Integer loctypIndex = LOC_TYP.getIndex();
        return Location.builder()
                .main(row.getCell(loctypIndex).getStringCellValue().equals("3"))
                .rangeGroup(row.getCell(RANGE_GROUP.getIndex()).getStringCellValue())
                .specshop(setSpecshop(row.getCell(SPECSHOP.getIndex()).getStringCellValue()))
                .name(row.getCell(SLID.getIndex()).getStringCellValue())
                .build();
    }

    private List<LocationHelper> sortSheetByLocation(Sheet sheet) {
        List<LocationHelper> locations = new ArrayList<>();

        int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();
        for (int i = Constants.SLM0003_ROW_INDEX +1; i<rows; i++){
            locations.add(new LocationHelper(i,sheet.getRow(i).getCell(SLID.getIndex()).getStringCellValue()));
        }
        return locations.stream()
                .sorted(Comparator.comparing(LocationHelper::getLocation))
                .collect(Collectors.toList());
    }

    private boolean rowsIsIgnored(Row row) {
        for (IgnoredSlm003Value ignoredSlm003Value : Constants.SLM0003_IGNORED_VALUES){
            String cellValue = row.getCell(ignoredSlm003Value.getColumn().getIndex()).getStringCellValue();
            if (cellValue.contains(ignoredSlm003Value.getValue())){
                return true;
            }
        }
        return false;
    }

    private IkeaProduct mapToProduct(Row row) throws IOException {
        Set<Location> locations = new HashSet<>();
        locations.add(
                buildLocation(row)
        );

        IkeaProduct product = IkeaProduct.builder()
                .id(row.getCell(NUMBER.getIndex()).getStringCellValue())
                .name(row.getCell(NAME.getIndex()).getStringCellValue())
                .locations(locations)
                .fcst((int) row.getCell(FCST.getIndex()).getNumericCellValue())
                .avgSales(row.getCell(AVGSALES.getIndex()).getNumericCellValue())
                .palQty((int) row.getCell(PAL_QTY.getIndex()).getNumericCellValue())
                .availableStock((int) row.getCell(AVAILABLE_STOCK.getIndex()).getNumericCellValue())
                .sgf((int) row.getCell(SGF_QTY.getIndex()).getNumericCellValue())
                .volume(mapToDoubleNumber(row.getCell(VOLUME.getIndex())))
                .build();

        product.addAssq((int) row.getCell(SSQ.getIndex()).getNumericCellValue());

        return product;
    }

    private Specshop setSpecshop(String specshop) {
        if (specshop.toLowerCase().startsWith("odd")) return Specshop.NIEPARZYSTA;
        if (specshop.toLowerCase().startsWith("even")) return Specshop.PARZYSTA;
        return Specshop.OTHER;
    }

    private Double mapToDoubleNumber(Cell cell) throws IOException {
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
