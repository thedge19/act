package com.act.main.controller;

import com.act.main.service.MainService;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "/main")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class MainController {

    private final MainService mainService;

    @GetMapping("/{monthId}")
    public void createPdfRegistry(@PathVariable int monthId) throws DocumentException, IOException {
        mainService.createRegistry(monthId);
    }


}
