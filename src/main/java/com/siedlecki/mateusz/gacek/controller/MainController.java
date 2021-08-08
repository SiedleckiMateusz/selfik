package com.siedlecki.mateusz.gacek.controller;

import com.siedlecki.mateusz.gacek.core.FileGeneratorService;
import com.siedlecki.mateusz.gacek.core.XlsxFileWriter;
import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import org.springframework.beans.factory.annotation.Autowired;
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


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

//@Controller
public class MainController {

//    private final FileGeneratorService service;
//
//    @Autowired
//    public MainController(FileGeneratorService service) {
//        this.service = service;
//    }
//
//    @GetMapping("/")
//    public String mainSite() {
//        return "index";
//    }
//
//    @GetMapping("prenotification-form")
//    public String prenotForm() {
//        return "prenotification_form";
//    }
//
//    @GetMapping("morning-order-form")
//    public String morningOrderForm() {
//        return "morning_order_form";
//    }
//
//    @GetMapping("morning-order-form-with-opq")
//    public String morningOrderWithOpqForm() {
//        return "morning_order_form_with_opq";
//    }
//
//    @PostMapping("generate-prenot-file")
//    public ResponseEntity<?> generatePrenotFile(
//            @RequestAttribute("slm0003") MultipartFile slm0003,
//            @RequestAttribute("prenot") MultipartFile prenot) {
//        try {
//            XlsxFileWriter result = null;
//
//            if (!slm0003.isEmpty() && !prenot.isEmpty()) {
////                result = service.prenotProcess(slm0003, prenot, false);
//            } else {
//                System.err.println("Nie dodano wymaganych plików. Spróbuj ponownie");
//            }
//            if (result != null && result.getWorkbook() != null) {
//                return sendReadyFile(result);
//            }
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            return ResponseEntity.of(Optional.of(e.getMessage()));
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//
//    @PostMapping("generate-morning-file")
//    public ResponseEntity<?> generateMorningFile(
//            @RequestAttribute("slm0003") MultipartFile slm0003) {
//        try {
//            XlsxFileWriter result = null;
//
//            if (!slm0003.isEmpty()) {
//                result = service.morningOrderProcess(slm0003, false);
//            } else {
//                System.err.println("Nie dodano wymaganego pliku. Spróbuj ponownie");
//            }
//            if (result != null && result.getWorkbook() != null) {
//                return sendReadyFile(result);
//            }
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            return ResponseEntity.of(Optional.of(e.getMessage()));
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//
//    @PostMapping("generate-morning-file-with-opq")
//    public ResponseEntity<?> generateMorningFile(
//            @RequestAttribute("slm0003") MultipartFile slm0003,
//            @RequestAttribute("opq") MultipartFile opq) {
//        try {
//            XlsxFileWriter result = null;
//
//            if (!slm0003.isEmpty() && !opq.isEmpty()) {
//                result = service.morningOrderProcess(slm0003,opq, false);
//            } else {
//                System.err.println("Nie dodano wymaganego pliku. Spróbuj ponownie");
//            }
//            if (result != null && result.getWorkbook() != null) {
//                return sendReadyFile(result);
//            }
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            e.printStackTrace();
//            return ResponseEntity.of(Optional.of(e.getMessage()));
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//
//    private ResponseEntity<ByteArrayResource> sendReadyFile(XlsxFileWriter result) throws IOException {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        HttpHeaders header = new HttpHeaders();
//        header.setContentType(new MediaType("application", "download"));
//        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + result.getFileName());
//        result.getWorkbook().write(outputStream);
//        result.getWorkbook().close();
//        return new ResponseEntity<>(new ByteArrayResource(outputStream.toByteArray()),
//                header, HttpStatus.CREATED);
//    }
}
