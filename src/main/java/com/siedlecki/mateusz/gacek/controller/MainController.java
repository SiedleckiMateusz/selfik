package com.siedlecki.mateusz.gacek.controller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Controller
public class MainController {

    @GetMapping("test")
    public String testSite() {
        return "test";
    }

    @GetMapping("/")
    public String mainSite() {
        return "index";
    }

    @PostMapping("generate")
    public String generateExcelFile(@RequestAttribute("slm0003") MultipartFile slm0003) {
        if (!slm0003.isEmpty()){
            System.err.println("Plik slm0003 istnieje");
            Workbook workbook = null;

            try(InputStream fileInputStream = slm0003.getInputStream()){
                String name = slm0003.getOriginalFilename();
                if (name != null){
                    if (name.endsWith(".xlsx")) {
                        workbook = new XSSFWorkbook(fileInputStream);
                    }else if (name.endsWith(".xls")) {
                        workbook = new HSSFWorkbook(fileInputStream);
                    }else {
                        System.err.println("Plik "+ name +" nie jest w formacie .xls lub .xlsx");
                    }
                }
            }catch (Exception e){
                System.err.println("Plik jest nie poprawny");
            }

            String sheetName = workbook.getSheetName(0);
            System.out.println("Sheet name: "+sheetName);

        }else {
            System.err.println("Plik slm0003 nie został znaleziony");
        }


        return "generating";
    }

}
