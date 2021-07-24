package com.siedlecki.mateusz.gacek.controller;

import com.siedlecki.mateusz.gacek.core.FileGeneratorService;
import com.siedlecki.mateusz.gacek.core.XlsxFileWriter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Controller
public class MainController {

    private final FileGeneratorService service;

    @Autowired
    public MainController(FileGeneratorService service) {
        this.service = service;
    }

    @GetMapping("test")
    public String testSite() {
        return "test";
    }

    @GetMapping("/")
    public String mainSite() {
        return "index";
    }

    @PostMapping("generate")
    public ResponseEntity<?> generateExcelFile(@RequestAttribute("slm0003") MultipartFile slm0003, @RequestAttribute("prenot") MultipartFile prenot) {
        try {
        XlsxFileWriter result = null;

        if (!slm0003.isEmpty() && !prenot.isEmpty()) {
            result = service.prenotProcess(slm0003, prenot, false);
        } else {
            System.err.println("Jeden z plików lub wszystkie nie zostały znalezione");
        }
        if (result != null && result.getWorkbook()!=null) {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                HttpHeaders header = new HttpHeaders();
                header.setContentType(new MediaType("application", "force-download"));
                header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+result.getFileName());
                result.getWorkbook().write(outputStream);
                result.getWorkbook().close();
                return new ResponseEntity<>(new ByteArrayResource(outputStream.toByteArray()),
                        header, HttpStatus.CREATED);
            }
        }catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.of(Optional.of(e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

//    @GetMapping("download")
//    public ResponseEntity<?> sendFile() {
//
//
////        try {
////            // get your file as InputStream
////            InputStream is = new FileInputStream(System.getProperty("user.dir")+"\\ustawienie-stolow.xlsx");
////            // copy it to response's OutputStream
////            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
////            response.flushBuffer();
////        } catch (IOException ex) {
////            System.err.println("Error writing file to output stream. Filename was 'ustawienie-stolow.xlsx'\n"+ ex);
////            throw new RuntimeException("IOError writing file to output stream");
////        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
}
