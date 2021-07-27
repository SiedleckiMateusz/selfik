package com.siedlecki.mateusz.gacek.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("error")
    public String errorSite() {
        return "error";
    }
}
