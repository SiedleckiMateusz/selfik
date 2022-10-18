package com.siedlecki.mateusz.gacek.core.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.siedlecki.mateusz.gacek.core.Constants;
import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.core.model.Result;
import com.siedlecki.mateusz.gacek.core.model.Specshop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PdfGeneratorService {
    public void generatePrenotDocument(Document document, PdfWriter instance, Result result, boolean printL23) throws DocumentException, IOException {
        PdfGenerator generator = new PdfGenerator(document,instance);
        generator.generatePageWithTable(
                false,
                "Nieparzyste",
                getValArtToPrepareFromOneSpecshop(result,Specshop.NIEPARZYSTA),
                Constants.HEADERS_FOR_TO_PREPARE_PDF,
                Constants.COLUMNS_SIZE_FOR_TO_PREPARE_PDF
        );
        generator.generatePageWithTable(
                true,
                "Parzyste",
                getValArtToPrepareFromOneSpecshop(result,Specshop.PARZYSTA),
                Constants.HEADERS_FOR_TO_PREPARE_PDF,
                Constants.COLUMNS_SIZE_FOR_TO_PREPARE_PDF
        );
        if (printL23){
            generator.generatePageWithTable(
                    true,
                    "L23",
                    getValArtL23(result),
                    Constants.HEADERS_FOR_L23_PDF,
                    Constants.COLUMNS_SIZE_FOR_L23_PDF
            );
        }
    }

    private List<List<String>> getValArtToPrepareFromOneSpecshop(Result result, Specshop specshop){
        List<IkeaProduct> nieparzyste = result.getToPrepare().stream()
                .filter(ip -> ip.getMainLocation().getSpecshop().equals(specshop))
                .sorted(Comparator.comparing(ikeaProduct -> ikeaProduct.getMainLocation().getName()))
                .collect(Collectors.toList());
        List<List<String>> listOfValues = new ArrayList<>();
        for (IkeaProduct p:nieparzyste){
            List<String> values = new ArrayList<>();
            values.add(p.getId());
            values.add(p.getName());
            values.add(p.locationsToString());
            values.add(String.format("%.2f",(double)p.l23OrderToFullPal()+p.prenotSalesPQ()));
            values.add(p.getStatus().getOpis());
            listOfValues.add(values);
        }
        return listOfValues;
    }

    private List<List<String>> getValArtL23(Result result){
        List<IkeaProduct> l23 = result.getToOrder().stream()
                .sorted(Comparator.comparing(ikeaProduct -> ikeaProduct.getMainLocation().getName()))
                .collect(Collectors.toList());
        List<List<String>> listOfValues = new ArrayList<>();
        for (IkeaProduct p:l23){
            List<String> values = new ArrayList<>();
            values.add(p.getName());
            values.add(p.getId());
            values.add(String.format("%.2f",(double)p.l23OrderToFullPal()));
            values.add(String.format("%.2f",p.prenotSalesPQ()));
            values.add(String.valueOf(p.l23AndPrenotOrderPalToQty()));
            listOfValues.add(values);
        }
        return listOfValues;
    }

}
