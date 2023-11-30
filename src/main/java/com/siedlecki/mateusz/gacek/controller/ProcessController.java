package com.siedlecki.mateusz.gacek.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.siedlecki.mateusz.gacek.xlsx.XlsGeneratorService;
import com.siedlecki.mateusz.gacek.model.ProductsContainer;
import com.siedlecki.mateusz.gacek.xlsx.XlsxFileWriter;
import com.siedlecki.mateusz.gacek.model.Result;
import com.siedlecki.mateusz.gacek.pdf.PdfGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@SessionScope
public class ProcessController extends MainController{
    private final ProductsContainer container = new ProductsContainer();
    private XlsxFileWriter fileWriter;
    private Result result;

    private final ProcessFlags flags;

    private final XlsGeneratorService service;

    private static final Logger log = LoggerFactory.getLogger(ProcessController.class);

    public ProcessController(XlsGeneratorService service) {
        this.flags = new ProcessFlags();
        this.service = service;
    }

    @GetMapping("/")
    public String mainSite() {
        log.info("Go to menu");
        reset();
        log.info("Reset files");
        return "index";
    }

    @GetMapping("summary")
    public String summary(Model model) {
        if (flags.isActiveAnyProcess()){
            model.addAttribute("toPrepare", result.getToPrepare());
            return "summary";
        }
        return "redirect:/";
    }

    public String process() {
        if (flags.isPrenotProcessFlag()) {
            return "redirect:/prenotification-process";
        }
        if (flags.isMorningProcessFlag()) {
            return "redirect:/morning-order-process";
        }
        return "redirect:/";
    }

    @GetMapping("morning-order-process")
    public String morningOrderProcess() {
        flags.setMorningProcessFlag(true);
        if (!flags.isSLM0003IsOkFlag()) {
            return "redirect:/slm0003-form";
        }
        if (!flags.isIv020IsOkFlag()) {
            return "redirect:/iv020-form";
        }
        result = service.getL23Products(container.getIkeaProductMap());

        return "redirect:/summary";
    }

    @GetMapping("prenotification-process")
    public String prenotOrderProcess() {
        flags.setPrenotProcessFlag(true);
        if (!flags.isSLM0003IsOkFlag()) {
            return "redirect:/slm0003-form";
        }
        if (!flags.isIv020IsOkFlag()) {
            return "redirect:/iv020-form";
        }
        if (!flags.isPrenotIsOkFlag()) {
            return "redirect:/prenot-form";
        }
        result = service.getL23AndPrenotProducts(container);


        return "redirect:/summary";
    }

    @GetMapping("slm0003-form")
    public String slm0003Form(Model model) {
        if (flags.isPrenotProcessFlag() || flags.isMorningProcessFlag()) {
            model.addAttribute("flags", flags);
            log.info("Go to SLM0003 formula");
            return "form/slm0003Form";
        }
        return "redirect:/";
    }

    @GetMapping("iv020-form")
    public String iv020Form(Model model) {
        if (flags.isPrenotProcessFlag() || flags.isMorningProcessFlag()) {
            model.addAttribute("flags", flags);
            log.info("Go to IV020 formula");
            return "form/iv020Form";
        }
        return "redirect:/";
    }

    @GetMapping("prenot-form")
    public String prenotForm(Model model) {
        if (flags.isPrenotProcessFlag()) {
            model.addAttribute("flags", flags);
            log.info("Go to PRENOT formula");
            return "form/prenotForm";
        }
        return "redirect:/";
    }

    @PostMapping("slm0003-file")
    public String processSLM0003(@RequestAttribute("slm0003") MultipartFile slm0003) {
        try {
            log.info("Recive slm0003 file");
            service.processSlm0003File(slm0003,container);
            log.info("Processed slm0003 file");
            flags.setSLM0003IsOkFlag(true);
            log.info("Set slm0003Flag on true");
        } catch (IOException e) {
            return "redirect:/slm0003-form?error";
        }

        return process();
    }

    @PostMapping("iv020-file")
    public String processIV020(@RequestAttribute("iv020") MultipartFile iv020) {
        try {
            log.info("Recive iv020 file");
            service.processIV002File(iv020,container);
            log.info("Processed iv020 file");
            flags.setIv020IsOkFlag(true);
            log.info("Set iv020Flag on true");
        } catch (IOException e) {
            return "redirect:/iv020-form?error";
        }
        return process();
    }

    @PostMapping("prenot-file")
    public String processPrenot(@RequestAttribute("prenot") MultipartFile prenot) {
        try {
            log.info("Recive Prenot file");
            container.setPrenotProductMap(service.processPrenotFile(prenot));
            log.info("Processed Prenot file");
            flags.setPrenotIsOkFlag(true);
            log.info("Set prenotFlag on true");
        } catch (IOException e) {
            return "redirect:/prenot-form?error";
        }
        return process();
    }
    @GetMapping("generate-excel")
    public String checkIfCanGenerateExcelFile(){
        if (flags.isActiveAnyProcess()){
            return "redirect:/generate-excel-file";
        }
        return "redirect:/";
    }

    @GetMapping("generate-excel-file")
    public ResponseEntity<?> generateExcelFile() {
        try {
            if (flags.isMorningProcessFlag()) {
                String fileName = "Seflik rano " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm")) + ".xlsx";
                fileWriter = service.generateXlsxFile(result, fileName);
            }
            if (flags.isPrenotProcessFlag()) {
                String fileName = "Selfik prenot " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm")) + ".xlsx";
                fileWriter = service.generateXlsxFile(result, fileName);
            }
            if (fileWriter != null && fileWriter.getWorkbook() != null) {
                return sendReadyFile(fileWriter);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            reset();
            return ResponseEntity.of(Optional.of(e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("generate-pdf")
    public String checkIfCanGeneratePdfFile(){
        if (flags.isActiveAnyProcess()){
            return "redirect:/generate-pdf-file";
        }
        return "redirect:/";
    }

    @GetMapping("generate-pdf-file")
    public ResponseEntity<?> printing() {
        Document document = new Document(PageSize.A4.rotate(),20,20,20,20);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfWriter instance = PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfGeneratorService pdfGeneratorService = new PdfGeneratorService();
            pdfGeneratorService.generatePrenotDocument(document,instance,result);
            document.close();
        } catch (DocumentException | IOException e) {
            System.err.println(e.getMessage());
            reset();
            return ResponseEntity.of(Optional.of(e.getMessage()));
        }

        String fileName = "Selfik " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm")) + ".pdf";
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
        return new ResponseEntity<>(new ByteArrayResource(outputStream.toByteArray()),
                header, HttpStatus.CREATED);
    }

    private void reset() {
        log.info("Restarting params");
        flags.reset();

        container.setIkeaProductMap(null);
        container.setPrenotProductMap(null);
        container.setLocations(null);
        fileWriter = null;
    }

    private ResponseEntity<ByteArrayResource> sendReadyFile(XlsxFileWriter result) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + result.getFileName());
        result.getWorkbook().write(outputStream);
        result.getWorkbook().close();
        return new ResponseEntity<>(new ByteArrayResource(outputStream.toByteArray()),
                header, HttpStatus.CREATED);
    }
}

