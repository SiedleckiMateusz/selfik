package com.siedlecki.mateusz.gacek.controller;

import com.siedlecki.mateusz.gacek.core.FileGeneratorService;
import com.siedlecki.mateusz.gacek.core.XlsxFileWriter;
import com.siedlecki.mateusz.gacek.core.model.IkeaProduct;
import com.siedlecki.mateusz.gacek.core.model.PrenotProduct;
import com.siedlecki.mateusz.gacek.core.model.Result;
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
import java.util.Map;
import java.util.Optional;

@Controller
@SessionScope
public class ProcessController {
    private Map<String, IkeaProduct> ikeaProductMap;
    private Map<String, PrenotProduct> prenotProductMap;
    private XlsxFileWriter fileWriter;

    private final ProcessFlags flags;

    private final FileGeneratorService service;

    private static final Logger log = LoggerFactory.getLogger(ProcessController.class);

    public ProcessController( FileGeneratorService service) {
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
        if (!flags.isOpqIsOkFlag()) {
            return "redirect:/opq-form";
        }

        String fileName = "Poranne zamówienie " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm")) + ".xlsx";
        try {
            Result result = service.getProductsToOrderAndPrepare(ikeaProductMap);
            fileWriter = service.generateXlsxFile(result,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/generate-file";
    }
    @GetMapping("prenotification-process")
    public String prenotOrderProcess() {
        flags.setPrenotProcessFlag(true);
        if (!flags.isSLM0003IsOkFlag()) {
            return "redirect:/slm0003-form";
        }
        if (!flags.isOpqIsOkFlag()) {
            return "redirect:/opq-form";
        }
        if (!flags.isPrenotIsOkFlag()) {
            return "redirect:/prenot-form";
        }
        String fileName = "Prenot " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm")) + ".xlsx";
        try {
            Result result = service.getProductsToOrderAndPrepare(ikeaProductMap, prenotProductMap);
            fileWriter = service.generateXlsxFile(result,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/generate-file";
    }

    @GetMapping("slm0003-form")
    public String slm0003Form(Model model) {
        model.addAttribute("flags",flags);
        log.info("Go to SLM0003 formula");
        return "form/slm0003Form";
    }
    @GetMapping("opq-form")
    public String opqForm(Model model) {
        model.addAttribute("flags",flags);
        log.info("Go to OPQ formula");
        return "form/opqForm";
    }
    @GetMapping("prenot-form")
    public String prenotForm(Model model){
        model.addAttribute("flags",flags);
        log.info("Go to PRENOT formula");
        return "form/prenotForm";
    }

    @PostMapping("slm0003-file")
    public String processSLM0003(@RequestAttribute("slm0003") MultipartFile slm0003) {
        try {
            log.info("Recive slm0003 file");
            ikeaProductMap = service.processSlm0003file(slm0003);
            log.info("Processed slm0003 file");
            flags.setSLM0003IsOkFlag(true);
            log.info("Set slm0003Flag on true");
        } catch (IOException e) {
            return "redirect:/slm0003-form?error";
        }

        return process();
    }
    @PostMapping("opq-file")
    public String processOpq(@RequestAttribute("opq") MultipartFile opq) {
        try {
            log.info("Recive OPQ file");
            ikeaProductMap = service.processOpq(opq, ikeaProductMap,getDaysToPick());
            log.info("Processed OPQ file");
            flags.setOpqIsOkFlag(true);
            log.info("Set OpqFlag on true");
        } catch (IOException e) {
            return "redirect:/opq-form?error";
        }

        return process();
    }
    @PostMapping("prenot-file")
    public String processPrenot(@RequestAttribute("prenot") MultipartFile prenot) {
        try {
            log.info("Recive Prenot file");
            prenotProductMap = service.processPrenotFile(prenot);
            log.info("Processed Prenot file");
            flags.setPrenotIsOkFlag(true);
            log.info("Set prenotFlag on true");
        } catch (IOException e) {
            return "redirect:/prenot-form?error";
        }
        return process();
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
        } finally {
            reset();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private void reset() {
        log.info("Restarting params");
        flags.reset();

        ikeaProductMap = null;
        prenotProductMap = null;
        fileWriter = null;
    }

    private int getDaysToPick() {
        if (flags.isPrenotProcessFlag()){
            return  2;
        }
        if (flags.isMorningProcessFlag()){
            return  1;
        }
        throw new IllegalStateException("prenot and morning Process flags are false! I don't know how many days to pick choose");
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

