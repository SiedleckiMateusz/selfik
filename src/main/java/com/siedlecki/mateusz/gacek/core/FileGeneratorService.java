package com.siedlecki.mateusz.gacek.core;

import com.siedlecki.mateusz.gacek.core.mapper.OpqMapper;
import com.siedlecki.mateusz.gacek.core.mapper.PrenotMapper;
import com.siedlecki.mateusz.gacek.core.mapper.Slm00003Mapper;
import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.core.model.PrenotProduct;
import com.siedlecki.mateusz.gacek.core.model.Result;
import com.siedlecki.mateusz.gacek.core.reader.SheetReader;
import com.siedlecki.mateusz.gacek.core.reader.SheetReaderFactory;
import com.siedlecki.mateusz.gacek.core.sheet.SheetContentGenerator;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static com.siedlecki.mateusz.gacek.core.sheet.SheetContentGenerator.*;

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
        writer = writer.addSheet(toPrepareSheetValues(result.getToPrepare()),
                        TO_PREPARE_COLUMNS,"Places to prepare")
                .addSheet(toOrderSheetValues(result.getToOrder()),
                        TO_ORDER_COLUMNS,"L23 order")
                .addSheet(allParamSheetValues(result.getAll()),
                        ALL_PARAM_COLUMNS,"All");

        return writer;
    }

    /* ******************************************************************************* */


//    private void saveOperationToStatistic(List<IkeaProduct> products,OperationType operationType) {
//        SavedOperationsReaderAndWriter opSum = new SavedOperationsReaderAndWriter();
//        OperationsResults operationsResults = new OperationsResults(LocalDateTime.now(), operationType, products);
//        opSum.saveOperation(operationsResults);
//    }

}
