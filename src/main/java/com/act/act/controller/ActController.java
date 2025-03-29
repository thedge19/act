package com.act.act.controller;

import com.act.act.dto.ActRequestDto;
import com.act.act.dto.ActResponseDto;
import com.act.act.dto.ActUpdateRequestDto;
import com.act.act.dto.ActUpdateResponseDto;
import com.act.act.model.Act;
import com.act.act.model.EntranceControl;
import com.act.act.model.SelectedPeriod;
import com.act.act.service.ActService;
import com.act.excel.service.ExcelService;
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

    @GetMapping("/entrance")
    public List<EntranceControl> getAllEntranceControl() {
        log.info("Get all Acts");
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
            @RequestBody SelectedPeriod selectedPeriod) throws IOException {
        excelService.writeExcel(selectedPeriod);
    }

    @DeleteMapping("/excel")
    public void deleteWorkbookSheets() throws IOException {
        excelService.removeSheets();
    }
}
