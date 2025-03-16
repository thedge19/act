package com.act.standard.controller;

import com.act.standard.model.Standard;
import com.act.standard.service.StandardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/standards")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class StandardController {

    private final StandardService standardService;

    @GetMapping("/{id}")
    public Standard get(@PathVariable Long id) {
        log.info("Get Standard by id: {}", id);
        Standard standard = standardService.get(id);
        log.info("Get Standard: {}", standard);
        return standard;
    }

    @GetMapping
    public List<Standard> getAll() {
        log.info("Get All Standards");
        return standardService.getAll();
    }

    @PostMapping
    public Standard create(
            @RequestBody Standard standard) {
        log.info("Create Standard: {}", standard.getName());
        Standard standardCreated = standardService.create(standard);
        log.info("Create Standard: {}", standardCreated);
        return standardCreated;
    }

    @PatchMapping("/{id}")
    public Standard update(@PathVariable long id,
                            @RequestBody Standard standard) {
        log.info("Update Standard: {}", standard.getName());
        Standard standardUpdated = standardService.update(id, standard);
        log.info("Update Standard: {}", standardUpdated);
        return standardUpdated;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Delete Standard: {}", id);
        standardService.delete(id);
        log.info("Standard with id: {} deleted", id);
    }
}
