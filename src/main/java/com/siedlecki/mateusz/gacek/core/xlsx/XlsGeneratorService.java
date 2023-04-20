package com.siedlecki.mateusz.gacek.core.xlsx;

import com.siedlecki.mateusz.gacek.core.IkeaProductService;
import com.siedlecki.mateusz.gacek.core.mapper.Iv020Mapper;
import com.siedlecki.mateusz.gacek.core.mapper.PrenotMapper;
import com.siedlecki.mateusz.gacek.core.mapper.Slm00003Mapper;
import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.core.model.PrenotProduct;
import com.siedlecki.mateusz.gacek.core.model.ProductsContainer;
import com.siedlecki.mateusz.gacek.core.model.Result;
import com.siedlecki.mateusz.gacek.core.reader.SheetReader;
import com.siedlecki.mateusz.gacek.core.reader.SheetReaderFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static com.siedlecki.mateusz.gacek.core.xlsx.XlsxSheetContentGenerator.*;

@Service
public class XlsGeneratorService {
    private final SheetReader slm0003Reader;
    private final SheetReader prenotReader;
    private final SheetReader iv020Reader;
    private final IkeaProductService ikeaProductService;

    {
        slm0003Reader = SheetReaderFactory.getSlm0003Reader();
        prenotReader = SheetReaderFactory.getPrenotReader();
        iv020Reader = SheetReaderFactory.getIv020Reader();
    }

    public XlsGeneratorService(IkeaProductService ikeaProductService) {
        this.ikeaProductService = ikeaProductService;
    }

    public void processSlm0003File(MultipartFile slm0003File, ProductsContainer container) throws IOException {
        Sheet slm0003Sheet = slm0003Reader.getCorrectSheetFromFile(slm0003File);
        Slm00003Mapper slm00003Mapper = new Slm00003Mapper(slm0003Sheet);
        container.setIkeaProductMap(slm00003Mapper.getProductMap());
        container.setLocations(slm00003Mapper.getLocations());
    }

    public void processIV002File(MultipartFile slm0003File, ProductsContainer container) throws IOException {
        Sheet iv020Sheet = iv020Reader.getCorrectSheetFromFile(slm0003File);
        Iv020Mapper iv020Mapper = new Iv020Mapper(iv020Sheet);
        iv020Mapper.procesResevationIkeaProducts(container.getIkeaProductMap());
    }

    public Map<String,PrenotProduct> processPrenotFile(MultipartFile prenotFile) throws IOException {
        Sheet prenotSheet = prenotReader.getCorrectSheetFromFile(prenotFile);
        return PrenotMapper.mapToPrenotProductMap(prenotSheet);
    }

    public Result getL23Products(Map<String,IkeaProduct> ikeaProductMap){
        return ikeaProductService.getL23Products(new ArrayList<>(ikeaProductMap.values()));
    }

    public Result getL23AndPrenotProducts(ProductsContainer container){
        return ikeaProductService.getL23AndPrenotProducts(container);
    }

    public XlsxFileWriter generateXlsxFile(Result result, String fileName) {
        XlsxFileWriter writer = new XlsxFileWriter(fileName);
        writer = writer.addSheet(toPrepareSheetValues(result.getToPrepare()),
                        TO_PREPARE_COLUMNS,"Preparing")
                .addSheet(allParamSheetValues(result.getAll()),
                        ALL_PARAM_COLUMNS,"All");

        return writer;
    }

}
