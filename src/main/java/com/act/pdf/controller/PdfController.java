package com.act.pdf.controller;

import com.act.pdf.service.PdfService;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "/toPdf")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class PdfController {
    private final PdfService pdfService;

    @GetMapping("/{monthId}")
    public void toPdf(@PathVariable int monthId) throws DocumentException, IOException {
        pdfService.exportToPdf(monthId);
    }
}
