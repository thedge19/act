package com.act.working.controller;

import com.act.working.dto.WorkingRequestDto;
import com.act.working.dto.WorkingUpdateDto;
import com.act.working.model.Working;
import com.act.working.service.WorkingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/workings")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class WorkingController {

    private final WorkingService workingService;

    @GetMapping("/working/{id}")
    public Working get(@PathVariable Long id) {
        log.info("Get Working by id: {}", id);
        Working working = workingService.get(id);
        log.info("Get Working: {}", working);
        return working;
    }

    @GetMapping("/{id}")
    public List<Working> getAll(@PathVariable Long id) {
        log.info("Get All by SubObject id: {}", id);
        return workingService.getAll(id);
    }

    @PostMapping
    public Working create(
            @RequestBody WorkingRequestDto workingDto) {
        log.info("Create Working: {}", workingDto.getName());
        Working workingCreated = workingService.create(workingDto);
        log.info("Create Working: {}", workingCreated);
        return workingCreated;
    }

    @PatchMapping("/{id}")
    public Working update(@PathVariable long id,
                          @RequestBody WorkingUpdateDto dto) {
        log.info("Update Working: {}", id);
        Working workingUpdated = workingService.update(id, dto);
        log.info("Update Working: {}", workingUpdated);
        return workingUpdated;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Delete Working: {}", id);
        workingService.delete(id);
        log.info("Working with id: {} deleted", id);
    }
}