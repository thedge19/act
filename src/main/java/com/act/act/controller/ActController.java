package com.act.act.controller;

import com.act.act.dto.*;
import com.act.act.model.Act;
import com.act.act.model.EntranceControl;
import com.act.act.model.SelectedPeriod;
import com.act.act.service.ActService;
import com.act.excel.service.ExcelService;
import com.act.pdf.service.PdfService;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/acts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class ActController {

    private final ActService actService;
    private final ExcelService excelService;
    private final PdfService pdfService;

    @GetMapping("/{id}")
    public ActResponseDto get(@PathVariable Long id) {
        log.info("Get Act by id: {}", id);
        ActResponseDto responseDto = actService.get(id);
        log.info("Get Act: {}", responseDto);
        return responseDto;
    }

    @GetMapping("/update/{id}")
    public ActUpdateResponseDto getUpdatedAct(@PathVariable long id) {
        return actService.getUpdatedAct(id);
    }

    @GetMapping
    public List<ActResponseDto> getAll() {
        return actService.getAll();
    }

    @GetMapping("/nullInRegistries")
    public List<ActResponseDto> getAllWithNullInRegistries() {
        List<ActResponseDto> responseDtos = actService.getAllWithNullInRegistries();
        log.info("Get Acts with null in registries: {}", responseDtos.size());
        return actService.getAllWithNullInRegistries();
    }

    @GetMapping("/entrance/{id}")
    public EntranceControl getEntranceControl(@PathVariable long id) {
        log.info("Get control with id: {}", id);
        return actService.findEntranceControl(id);
    }

    @GetMapping("/entrance")
    public List<EntranceControl> getEntranceControls() {
        return actService.getAllEntranceControl();
    }


    @PostMapping
    public Act create(
            @RequestBody ActRequestDto requestDto) {
        log.info("Create Act: {}", requestDto);
        Act actCreated = actService.create(requestDto);
        log.info("Create Act: {}", actCreated);
        return actCreated;
    }

    @PatchMapping("/{id}")
    public Act update(@PathVariable long id,
                            @RequestBody ActUpdateRequestDto requestDto) {
        Act actUpdated = actService.update(id, requestDto);
        log.info("Update Act: {}", actUpdated);
        return actUpdated;
    }

    @PatchMapping("/entrance/{id}")
    public EntranceControl updateEntranceControl(@PathVariable long id,
                            @RequestBody EntranceControlRequestDto requestDto) {
        EntranceControl entranceControl = actService.updateEntranceControl(id, requestDto);
        log.info("Update Act: {}", entranceControl.getId());
        return entranceControl;
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Delete Act: {}", id);
        actService.delete(id);
        log.info("Act with id: {} deleted", id);
    }

    @DeleteMapping("/entrance/{id}")
    public void deleteControl(@PathVariable Long id) {
        log.info("Delete EntranceControlAct: {}", id);
        actService.deleteControl(id);
        log.info("EntranceControlAct: with id: {} deleted", id);
    }

    @PostMapping("/excel")
    public void writeExcelFile(
            @RequestBody SelectedPeriod selectedPeriod) throws IOException, DocumentException {
        pdfService.exportAOSRtoPdf(selectedPeriod);
    }

    @GetMapping("/excelControl")
    public void writeExcelControlFile() throws IOException {
        excelService.writeExcelControl();
    }

    @DeleteMapping("/excel")
    public void deleteWorkbookSheets() throws IOException {
        excelService.removeSheets();
    }

    @GetMapping("/filterBySubObject")
    public List<ActResponseDto> filterBySubObject() {
        return actService.filterBySubObject();
    }
}
