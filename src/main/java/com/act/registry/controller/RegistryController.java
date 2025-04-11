package com.act.registry.controller;

import com.act.excel.service.ExcelService;
import com.act.registry.dto.RegistryDto;
import com.act.registry.dto.RegistryRequestDto;
import com.act.registry.dto.RegistryResponseDto;
import com.act.registry.dto.RegistryUpdateRequestDto;
import com.act.registry.service.RegistryService;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/registries")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class RegistryController {

    private final RegistryService registryService;

    @GetMapping("/{monthId}")
    public List<RegistryResponseDto> getAllByMonth(@PathVariable int monthId) {
        return registryService.getAllByMonth(monthId);
    }

    @PostMapping
    public void create(@RequestBody RegistryDto dto) {
        log.info("Create registry: {}", dto);
        registryService.create(dto);
    }

    @PatchMapping
    public void update(@RequestBody List<RegistryUpdateRequestDto> dtos) {
        registryService.update(dtos);
    }

    @PatchMapping("/{id}")
    public void updateNumberOfPages(@PathVariable long id,
                                    @RequestBody int numberOfSheets) {
        log.info("Update number of sheets: {}", numberOfSheets);
        registryService.updateNumberOfPages(id, numberOfSheets);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Delete registry: {}", id);
        registryService.delete(id);
    }

    @GetMapping("/excel/{monthId}")
    public void exportExcel(@PathVariable int monthId) throws IOException, DocumentException, InvalidFormatException {
        registryService.excelExport(monthId);
    }
}
