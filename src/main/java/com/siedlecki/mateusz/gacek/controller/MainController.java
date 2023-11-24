package com.siedlecki.mateusz.gacek.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @ExceptionHandler(Exception.class)
    public String handleFileSizeLimitExceeded(Exception exc, Model model) {
        model.addAttribute("errorMsg", exc.getMessage());
        model.addAttribute("errorStackTrace", exc.getStackTrace());
        return "/error";
    }

}
