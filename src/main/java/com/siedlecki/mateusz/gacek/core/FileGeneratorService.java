package com.siedlecki.mateusz.gacek.core;

import com.siedlecki.mateusz.gacek.core.mapper.PrenotMapper;
import com.siedlecki.mateusz.gacek.core.mapper.Slm00003Mapper;
import com.siedlecki.mateusz.gacek.core.model.IkeaProductToWrite;
import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.core.model.PrenotProduct;
import com.siedlecki.mateusz.gacek.core.reader.SheetReader;
import com.siedlecki.mateusz.gacek.core.reader.SheetReaderFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class FileGeneratorService {

    public XlsxFileWriter prenotProcess(MultipartFile slm0003File, MultipartFile prenotFile, boolean saveToStats) throws IOException {
        String fileName = "Prenot " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm")) + ".xlsx";

        SheetReader slm0003Reader = SheetReaderFactory.getSlm0003Reader();
        SheetReader prenotReader = SheetReaderFactory.getPrenotReader();

        XlsxFileWriter writer = new XlsxFileWriter(fileName);
        Sheet slm0003Sheet = slm0003Reader.getSheetFromFile(slm0003File);
        Sheet prenotSheet = prenotReader.getSheetFromFile(prenotFile);
        List<IkeaProduct> ikeaProducts = Slm00003Mapper.mapToProductList(slm0003Sheet);
        List<PrenotProduct> prenotProducts = PrenotMapper.mapToProductList(prenotSheet);

        IkeaProductFilter ikeaProductFilter = new IkeaProductFilter();
        Map<String, List<IkeaProductToWrite>> map = ikeaProductFilter.getProductsToPrepareAndExtraOrder(ikeaProducts, prenotProducts);
        writer = writer.addSheet(map.get("toPrepare"), "Places to prepare")
                .addSheet(map.get("toOrder"), "L23 extra order");

//            if (saveToStats){
//                List<IkeaProduct> toOrder = map.get("toOrder").stream().map(IkeaFileProduct::getProduct).collect(Collectors.toList());
//                saveOperationToStatistic(toOrder,OperationType.PRENOT);
//            }

        return writer;
    }

    public XlsxFileWriter morningOrderPorocess(MultipartFile slm0003File, boolean saveToStats) throws IOException {
        String fileName = "Poranne zamówienie " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm")) + ".xlsx";
        SheetReader slm0003Reader = SheetReaderFactory.getSlm0003Reader();

        XlsxFileWriter writer = null;

        writer = new XlsxFileWriter(fileName);
        Sheet slm0003Sheet = slm0003Reader.getSheetFromFile(slm0003File);
        List<IkeaProduct> ikeaProducts = Slm00003Mapper.mapToProductList(slm0003Sheet);

        IkeaProductFilter ikeaProductFilter = new IkeaProductFilter();
        Map<String, List<IkeaProductToWrite>> map = ikeaProductFilter.getProductsToOrderAndPrepare(ikeaProducts);

        writer = writer.addSheet(map.get("toOrder"), "L23 order")
                .addSheet(map.get("toPrepareFromOrder"), "Place to prepare");
//            if (saveToStats){
//                List<IkeaProduct> toOrder = map.get("toOrder").stream().map(IkeaFileProduct::getProduct).collect(Collectors.toList());
//                saveOperationToStatistic(toOrder, OperationType.MORNING_ORDER);
//            }


        return writer;
    }

//    private void saveOperationToStatistic(List<IkeaProduct> products,OperationType operationType) {
//        SavedOperationsReaderAndWriter opSum = new SavedOperationsReaderAndWriter();
//        OperationsResults operationsResults = new OperationsResults(LocalDateTime.now(), operationType, products);
//        opSum.saveOperation(operationsResults);
//    }

}
