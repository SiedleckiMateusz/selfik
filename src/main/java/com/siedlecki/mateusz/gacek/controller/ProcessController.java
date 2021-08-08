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

    private boolean SLM0003IsOkFlag;
    private boolean prenotIsOkFlag;
    private boolean opqIsOkFlag;

    private boolean prenotProcessFlag;
    private boolean morningProcessFlag;

    private final FileGeneratorService service;

    private static final Logger log = LoggerFactory.getLogger(ProcessController.class);

    public ProcessController(FileGeneratorService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String mainSite() {
        log.info("Go to menu");
        reset();
        log.info("Reset files");
        return "index";
    }


    @GetMapping("process")
    public String process() {
        if (prenotProcessFlag) {
            return "redirect:/prenotification";
        }
        if (morningProcessFlag) {
            return "redirect:/morning-order";
        }
        return "redirect:/";
    }

    @GetMapping("morning-order")
    public String morningOrderProcess() {
        morningProcessFlag = true;
        if (!SLM0003IsOkFlag) {
            return "form/slm0003Form";
        }
        if (!opqIsOkFlag) {
            return "form/opqForm";
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

    @GetMapping("prenotification")
    public String prenotOrderProcess() {
        prenotProcessFlag = true;
        if (!SLM0003IsOkFlag) {
            return "redirect:/slm0003Form";
        }
        if (!opqIsOkFlag) {
            return "redirect:/opqForm";
        }
        if (!prenotIsOkFlag) {
            return "redirect:/prenotForm";
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

    private void reset() {
        log.info("Restarting params");
        SLM0003IsOkFlag = false;
        opqIsOkFlag = false;
        prenotIsOkFlag = false;

        ikeaProductMap = null;
        prenotProductMap = null;
        fileWriter = null;

        prenotProcessFlag = false;
        morningProcessFlag = false;
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

    @GetMapping("slm0003Form")
    public String slm0003Form() {
        log.info("Go to SLM0003 formula");
        return "form/slm0003Form";
    }

    @GetMapping("opqForm")
    public String opqForm() {
        log.info("Go to OPQ formula");
        return "form/opqForm";
    }

    @GetMapping("prenotForm")
    public String prenotForm() {
        log.info("Go to PRENOT formula");
        return "form/prenotForm";
    }

    @PostMapping("slm0003File")
    public String processSLM0003(@RequestAttribute("slm0003") MultipartFile slm0003) {
        try {
            log.info("Recive slm0003 file");
            ikeaProductMap = service.processSlm0003file(slm0003);
            log.info("Processed slm0003 file");
            SLM0003IsOkFlag = true;
            log.info("Set slm0003Flag on true");
        } catch (IOException e) {
            return "redirect:/slm0003Form?error";
        }

        return "redirect:/process";
    }

    @PostMapping("opqFile")
    public String processOpq(@RequestAttribute("opq") MultipartFile opq) {
        try {
            log.info("Recive OPQ file");
            ikeaProductMap = service.processOpq(opq, ikeaProductMap);
            log.info("Processed OPQ file");
            opqIsOkFlag = true;
            log.info("Set OpqFlag on true");
        } catch (IOException e) {
            return "redirect:/opqForm?error";
        }

        return "redirect:/process";
    }

    @PostMapping("prenotFile")
    public String processPrenot(@RequestAttribute("prenot") MultipartFile prenot) {
        try {
            log.info("Recive Prenot file");
            prenotProductMap = service.processPrenotFile(prenot);
            log.info("Processed Prenot file");
            prenotIsOkFlag = true;
            log.info("Set prenotFlag on true");
        } catch (IOException e) {
            return "redirect:/prenotForm?error";
        }
        return "redirect:/process";
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

