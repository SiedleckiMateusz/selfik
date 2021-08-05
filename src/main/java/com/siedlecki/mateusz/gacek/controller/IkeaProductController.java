package com.siedlecki.mateusz.gacek.controller;

import com.siedlecki.mateusz.gacek.core.FileGeneratorService;
import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
@SessionScope
public class IkeaProductController {
    Map<String, IkeaProduct> productMap;
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
        SLM0003Flag = false;
        opqFlag = false;

        return "summary";
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
}

