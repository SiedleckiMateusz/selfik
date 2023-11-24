package com.siedlecki.mateusz.gacek.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InstructionsController extends MainController {

    @GetMapping("instruction")
    public String instruction(){
        return "instruction";
    }
}
