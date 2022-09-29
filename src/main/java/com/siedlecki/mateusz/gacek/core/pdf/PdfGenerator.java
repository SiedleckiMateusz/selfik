package com.siedlecki.mateusz.gacek.core.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.util.List;

public class PdfGenerator {
    private final Document document;
    private final PdfWriter instance;
    private final Font titleFont;
    private final Font tableFont;

    public PdfGenerator(Document document, PdfWriter instance) throws DocumentException, IOException {
        this.document = document;
        this.instance = instance;
        this.titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        BaseFont bf = BaseFont.createFont("src/main/resources/static/fonts/Montserrat-Light.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        this.tableFont = new Font(bf);
    }

    public void generatePageWithTable(boolean newPage,
                                      String naglowek,
                                      List<List<String>> listOfValues,
                                      List<String> headerNames,
                                      float[] headerSizes) throws DocumentException {
        if (newPage) {
            document.newPage();
        }
        if (naglowek != null) {
            createHeaderName(naglowek);
        }
        createTable(listOfValues, headerNames, headerSizes);


        int currentPageNumber = instance.getCurrentPageNumber();
        if (currentPageNumber % 2 == 1) {
            document.newPage();
            document.add(new Paragraph(" "));
        }
    }

    private void createHeaderName(String nazwa) throws DocumentException {

        Paragraph parzysteParagraf = new Paragraph();
        parzysteParagraf.add(new Paragraph(nazwa, titleFont));
        parzysteParagraf.add(new Paragraph(" "));

        document.add(parzysteParagraf);
    }

    private void createTable(List<List<String>> listOfValues,
                             List<String> headerNames,
                             float[] headerSizes) throws DocumentException {
        PdfPTable table = createTableWithHeaders(headerNames, headerSizes);
        fillTable(table, listOfValues);
        table.setWidthPercentage(100);
        document.add(table);
    }

    private void fillTable(PdfPTable table, List<List<String>> listOfValues) {
        for (List<String> p : listOfValues) {
            filRow(table, p);
        }
    }

    private void filRow(PdfPTable table, List<String> values) {
        for (String val : values) {
            table.addCell(new Phrase(val, tableFont));
        }
    }

    private PdfPTable createTableWithHeaders(List<String> headerNames, float[] hedersSize) {
        PdfPTable table = new PdfPTable(hedersSize);
        PdfPCell c1 = new PdfPCell();
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        for (String header : headerNames) {
            c1.setPhrase(new Phrase(header));
            table.addCell(c1);
        }
        table.setHeaderRows(1);

        return table;
    }
}
