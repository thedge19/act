package com.act.pdf.controller;

import com.act.act.model.SelectedPeriod;
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

//    @GetMapping("/{monthId}")
//    public void toPdf(@PathVariable int monthId) throws DocumentException, IOException {
//        pdfService.exportRegistryToPdf(monthId, path);
//    }

//    @GetMapping
//    public void numberOfPages() throws IOException {
//        pdfService.numberOfPages();
//    }

    @GetMapping("/workLog3")
    public void toPdf3() throws DocumentException, IOException {
        pdfService.exportWorkLog3ToPdf();
    }

    @GetMapping("/workLog6")
    public void toPdf6() throws DocumentException, IOException {
        pdfService.exportWorkLog6ToPdf();
    }

    @GetMapping("/workLog/{doc}")
    public void createWorkLog(@PathVariable int doc) throws DocumentException, IOException {
        pdfService.mergeUsingIText(doc);
    }

    @GetMapping("/entranceControlLog")
    public void createEntranceControlLog() throws DocumentException, IOException {
        pdfService.exportEntranceControlLogToPdf();
    }

//    @PostMapping("/excel")
//    public void writePdfAOSR(
//            @RequestBody SelectedPeriod selectedPeriod) throws IOException {
//        pdfService.writeExcel(selectedPeriod);
//    }

}
