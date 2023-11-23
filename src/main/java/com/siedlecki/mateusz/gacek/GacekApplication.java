package com.siedlecki.mateusz.gacek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@SpringBootApplication
public class GacekApplication {

    public static void main(String[] args) {
        SpringApplication.run(GacekApplication.class, args);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileSizeLimitExceeded(MaxUploadSizeExceededException exc, Model model) {
        model.addAttribute("errorMsg", exc.getMessage());
        return "redirect:/error";
    }

}
