package com.siedlecki.mateusz.gacek.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.siedlecki.mateusz.gacek.settings.Utils;
import com.siedlecki.mateusz.gacek.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.model.Result;
import com.siedlecki.mateusz.gacek.model.Specshop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PdfGeneratorService {

    public void generatePrenotDocument(Document document, PdfWriter instance, Result result) throws DocumentException, IOException {
        PdfGenerator generator = new PdfGenerator(document,instance);
        generator.generatePageWithTable(
                false,
                "Nieparzyste",
                getValArtToPrepareFromOneSpecshop(result,Specshop.NIEPARZYSTA),
                Utils.HEADERS_FOR_TO_PREPARE_PDF,
                Utils.COLUMNS_SIZE_FOR_TO_PREPARE_PDF
        );
        generator.generatePageWithTable(
                true,
                "Parzyste",
                getValArtToPrepareFromOneSpecshop(result,Specshop.PARZYSTA),
                Utils.HEADERS_FOR_TO_PREPARE_PDF,
                Utils.COLUMNS_SIZE_FOR_TO_PREPARE_PDF
        );
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
            values.add(p.getPalQty().toString());
            values.add(getQtyToOrderOrPropably(p));
            values.add(p.getStatus().getOpis());
            values.add(p.locationsToString());
            values.add(p.getName());
            listOfValues.add(values);
        }
        return listOfValues;
    }

    private String getQtyToOrderOrPropably(IkeaProduct p) {
        double v = (double) p.l23OrderToFullPal() + p.prenotSalesPQ();
        if (v>0) {
            return String.format("%.2f", v);
        }
        int i = (p.l23Order() + p.getReserved()) / p.getPalQty();
        return String.format("%.2f", (double)i);
    }

}
