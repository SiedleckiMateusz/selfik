package com.siedlecki.mateusz.gacek.core.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.core.model.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PdfGenerator {
    private final Document document;
    private final Font titleFont;

    public PdfGenerator(Document document) {
        this.document = document;
        this.titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    }

    public void generate(Result result) throws DocumentException {
        // TODO: 10.09.2022 Uporządkować ten generator i stworzyć serwis do generowania odpowiednich plików
        createHeaderName("Parzyste");
        List<IkeaProduct> parzyste = result.getToPrepare().stream()
                .filter(ip -> ip.getMainLocation().getSpecshop().equalsIgnoreCase("P"))
                .sorted(Comparator.comparing(ikeaProduct -> ikeaProduct.getMainLocation().getName()))
                .collect(Collectors.toList());
        createTable(document,parzyste);
        document.newPage();
        createHeaderName("Nieparzyste");
        List<IkeaProduct> nieparzyste = result.getToPrepare().stream()
                .filter(ip -> ip.getMainLocation().getSpecshop().equalsIgnoreCase("NP"))
                .sorted(Comparator.comparing(ikeaProduct -> ikeaProduct.getMainLocation().getName()))
                .collect(Collectors.toList());
        createTable(document,nieparzyste);
    }

    private void createHeaderName(String nazwa) throws DocumentException {

        Paragraph parzysteParagraf = new Paragraph();
        parzysteParagraf.add(new Paragraph(nazwa,titleFont));
        parzysteParagraf.add(new Paragraph(" "));

        document.add(parzysteParagraf);
    }

    private void createTable(Document document, List<IkeaProduct> ikeaProductList) throws DocumentException {

        PdfPTable table = createTableWithHeaders();
        fillTable(table,ikeaProductList);
        table.setWidthPercentage(100);
        document.add(table);
    }

    private void fillTable(PdfPTable table, List<IkeaProduct> ikeaProductList) {
        for (IkeaProduct p:ikeaProductList){
            List<String> values = new ArrayList<>();
            values.add(p.getId());
            values.add(p.getName());
            values.add(p.locationsToString());
            values.add(String.valueOf((double)p.l23OrderToFullPal()+p.prenotSalesPQ()));
            values.add(p.getStatus().getOpis());
            filRow(table,values);
        }
    }

    private void filRow(PdfPTable table, List<String> values) {
        for (String val: values){
            table.addCell(val);
        }
    }

    private PdfPTable createTableWithHeaders() {
        PdfPTable table = new PdfPTable(new float[]{8,33,44,6,9});
        PdfPCell c1 = new PdfPCell();
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        List<String> headers = Arrays.asList("NR","NAZWA","SLID","PALET","STATUS");
        for (String header:headers){
            c1.setPhrase(new Phrase(header));
            table.addCell(c1);
        }
        table.setHeaderRows(1);

        return table;
    }
}
