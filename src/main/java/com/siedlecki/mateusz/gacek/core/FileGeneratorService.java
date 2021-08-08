package com.siedlecki.mateusz.gacek.core;

import com.siedlecki.mateusz.gacek.core.mapper.OpqMapper;
import com.siedlecki.mateusz.gacek.core.mapper.PrenotMapper;
import com.siedlecki.mateusz.gacek.core.mapper.Slm00003Mapper;
import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.core.model.PrenotProduct;
import com.siedlecki.mateusz.gacek.core.model.Result;
import com.siedlecki.mateusz.gacek.core.reader.SheetReader;
import com.siedlecki.mateusz.gacek.core.reader.SheetReaderFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Service
public class FileGeneratorService {
    private final SheetReader slm0003Reader;
    private final SheetReader prenotReader;
    private final SheetReader opqReader;
    private final IkeaProductProcessor ikeaProductProcessor;

    {
        slm0003Reader = SheetReaderFactory.getSlm0003Reader();
        prenotReader = SheetReaderFactory.getPrenotReader();
        opqReader = SheetReaderFactory.getOpqReader();
    }

    public FileGeneratorService(IkeaProductProcessor ikeaProductProcessor) {
        this.ikeaProductProcessor = ikeaProductProcessor;
    }

    public Map<String,IkeaProduct> processSlm0003file(MultipartFile slm0003File) throws IOException {
        Sheet slm0003Sheet = slm0003Reader.getSheetFromFile(slm0003File);
        return Slm00003Mapper.mapToProductsMap(slm0003Sheet);
    }

    public Map<String,PrenotProduct> processPrenotFile(MultipartFile prenotFile) throws IOException {
        Sheet prenotSheet = prenotReader.getSheetFromFile(prenotFile);
        return PrenotMapper.mapToPrenotProductMap(prenotSheet);
    }

    public Map<String,IkeaProduct> processOpq(MultipartFile opqFile,Map<String,IkeaProduct>ikeaProductMap) throws IOException {
        Sheet opqSheet = opqReader.getSheetFromFile(opqFile);
        return OpqMapper.processPickingProduct(opqSheet,ikeaProductMap);
    }

    public Result getProductsToOrderAndPrepare(Map<String,IkeaProduct> ikeaProductMap){
        return ikeaProductProcessor.getProductsToOrderAndPrepare(new ArrayList<>(ikeaProductMap.values()));
    }

    public Result getProductsToOrderAndPrepare(
            Map<String,IkeaProduct> ikeaProducts, Map<String,PrenotProduct> prenotProducts){
        return ikeaProductProcessor.getProductsToPrepareAndExtraOrder(new ArrayList<>(ikeaProducts.values()),new ArrayList<>(prenotProducts.values()));
    }

    public XlsxFileWriter generateXlsxFile(Result result,String fileName) throws IOException {
        XlsxFileWriter writer = new XlsxFileWriter(fileName);
        writer = writer.addSheet(result.getToPrepare(), "Places to prepare")
                .addSheet(result.getToOrder(), "L23 order");

        return writer;
    }

    /* ******************************************************************************* */

//    public XlsxFileWriter getXlsxFile(Map<String,IkeaProduct> ikeaProductMap) throws IOException {
//        Map<String, List<IkeaProduct>> map = ikeaProductProcessor.getProductsToOrderAndPrepare(new ArrayList<>(ikeaProductMap.values()));
//
//        String fileName = "Poranne zamówienie z OPQ " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm")) + ".xlsx";
//        XlsxFileWriter writer = new XlsxFileWriter(fileName);
//        writer = writer.addSheet(map.get("toPrepareFromOrder"), "Places to prepare")
//                .addSheet(map.get("toOrder"), "L23 order");
//
//        return writer;
//    }
//
//
//    public XlsxFileWriter prenotProcess(MultipartFile slm0003File, MultipartFile prenotFile, boolean saveToStats) throws IOException {
//
//        Sheet slm0003Sheet = slm0003Reader.getSheetFromFile(slm0003File);
//        Sheet prenotSheet = prenotReader.getSheetFromFile(prenotFile);
//        List<IkeaProduct> ikeaProducts = new ArrayList<>(Slm00003Mapper.mapToProductsMap(slm0003Sheet).values());
//        List<PrenotProduct> prenotProducts = new ArrayList<>(PrenotMapper.mapToPrenotProductMap(prenotSheet).values());
//
//        IkeaProductProcessor ikeaProductProcessor = new IkeaProductProcessor();
//        Map<String, List<IkeaProduct>> map = ikeaProductProcessor.getProductsToPrepareAndExtraOrder(ikeaProducts, prenotProducts);
//
//        String fileName = "Prenot " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm")) + ".xlsx";
//        XlsxFileWriter writer = new XlsxFileWriter(fileName);
//        writer = writer.addSheet(map.get("toPrepare"), "Places to prepare")
//                .addSheet(map.get("toOrder"), "L23 extra order");
//
////            if (saveToStats){
////                List<IkeaProduct> toOrder = map.get("toOrder").stream().map(IkeaFileProduct::getProduct).collect(Collectors.toList());
////                saveOperationToStatistic(toOrder,OperationType.PRENOT);
////            }
//
//        return writer;
//    }
//
//    public XlsxFileWriter morningOrderProcess(MultipartFile slm0003File, boolean saveToStats) throws IOException {
//        Sheet slm0003Sheet = slm0003Reader.getSheetFromFile(slm0003File);
//        List<IkeaProduct> ikeaProducts = new ArrayList<>(Slm00003Mapper.mapToProductsMap(slm0003Sheet).values());
//
//        Map<String, List<IkeaProduct>> map = ikeaProductProcessor.getProductsToOrderAndPrepare(ikeaProducts);
//
//        String fileName = "Poranne zamówienie " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm")) + ".xlsx";
//        XlsxFileWriter writer = new XlsxFileWriter(fileName);
//        writer = writer.addSheet(map.get("toPrepareFromOrder"), "Places to prepare")
//                .addSheet(map.get("toOrder"), "L23 order");
//
////            if (saveToStats){
////                List<IkeaProduct> toOrder = map.get("toOrder").stream().map(IkeaFileProduct::getProduct).collect(Collectors.toList());
////                saveOperationToStatistic(toOrder, OperationType.MORNING_ORDER);
////            }
//
//        return writer;
//    }
//
//    public XlsxFileWriter morningOrderProcess(MultipartFile slm0003File, MultipartFile opqFile, boolean saveToStats) throws IOException {
//        Sheet slm0003Sheet = slm0003Reader.getSheetFromFile(slm0003File);
//        Sheet opqSheet = opqReader.getSheetFromFile(opqFile);
//        Map<String, IkeaProduct> ikeaProductMap = Slm00003Mapper.mapToProductsMap(slm0003Sheet);
//        Map<String, IkeaProduct> opqIkeaProductMap = OpqMapper.processPickingProduct(opqSheet, ikeaProductMap);
//
//        Map<String, List<IkeaProduct>> map = ikeaProductProcessor.getProductsToOrderAndPrepare(new ArrayList<>(opqIkeaProductMap.values()));
//
//        String fileName = "Poranne zamówienie z OPQ " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm")) + ".xlsx";
//        XlsxFileWriter writer = new XlsxFileWriter(fileName);
//        writer = writer.addSheet(map.get("toPrepareFromOrder"), "Places to prepare")
//                .addSheet(map.get("toOrder"), "L23 order");
//
////            if (saveToStats){
////                List<IkeaProduct> toOrder = map.get("toOrder").stream().map(IkeaFileProduct::getProduct).collect(Collectors.toList());
////                saveOperationToStatistic(toOrder, OperationType.MORNING_ORDER);
////            }
//
//        return writer;
//    }

//    private void saveOperationToStatistic(List<IkeaProduct> products,OperationType operationType) {
//        SavedOperationsReaderAndWriter opSum = new SavedOperationsReaderAndWriter();
//        OperationsResults operationsResults = new OperationsResults(LocalDateTime.now(), operationType, products);
//        opSum.saveOperation(operationsResults);
//    }

}
