package com.siedlecki.mateusz.gacek.controller;

import com.siedlecki.mateusz.gacek.core.FileGeneratorService;
import com.siedlecki.mateusz.gacek.core.XlsxFileWriter;
import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Controller
@SessionScope
public class IkeaProductController {
    private Map<String, IkeaProduct> productMap;
    private XlsxFileWriter fileWriter = null;
    private boolean SLM0003Flag;
    private boolean prenotFlag;
    private boolean opqFlag;

    private final FileGeneratorService service;

    public IkeaProductController(FileGeneratorService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String mainSite() {
        return "index";
    }

    @GetMapping("morning-order-form-with-opq")
    public String morningOrderWithOpqForm() {
        if (!SLM0003Flag) {
            return "slm0003Form";
        }
        if (!opqFlag) {
            return "opqForm";
        }

        try {
            fileWriter = service.getXlsxFile(productMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SLM0003Flag = false;
        opqFlag = false;
        productMap = null;

        if (fileWriter!=null){
            return "redirect:/generate-file";
        }
        return "redirect:/error";

    }

    @GetMapping("generate-file")
    public ResponseEntity<?> generateFile() {
        try {
            if (fileWriter != null && fileWriter.getWorkbook() != null) {
                return sendReadyFile(fileWriter);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.of(Optional.of(e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("slm0003Form")
    public String slm0003Form(){
        return "slm0003Form";
    }

    @GetMapping("opqForm")
    public String opqForm(){
        return "opqForm";
    }


    @PostMapping("slm0003File")
    public String processSLM0003(@RequestAttribute("slm0003") MultipartFile slm0003) {
        try {
            productMap = service.processSlm0003file(slm0003);
            SLM0003Flag = true;
        } catch (IOException e) {
            return "redirect:/slm0003Form?error";
        }

        return "redirect:/morning-order-form-with-opq";
    }

    @PostMapping("opqFile")
    public String processOpq(@RequestAttribute("opq") MultipartFile opq) {
        try {
            productMap = service.processOpq(opq, productMap);
            opqFlag = true;
        } catch (IOException e) {
            return "redirect:/opqForm?error";
        }

        return "redirect:/morning-order-form-with-opq";
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

