package com.act.working.controller;

import com.act.working.dto.WorkingRequestDto;
import com.act.working.dto.WorkingResponseDto;
import com.act.working.service.WorkingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/workings")
@RequiredArgsConstructor
@Slf4j
public class WorkingController {

    private final WorkingService workingService;

    @GetMapping("/{id}")
    public WorkingResponseDto get(@PathVariable Long id) {
        log.info("Get Working by id: {}", id);
        WorkingResponseDto workingDto = workingService.get(id);
        log.info("Get Working: {}", workingDto);
        return workingDto;
    }

    @PostMapping
    public WorkingResponseDto create(
            @RequestBody WorkingRequestDto workingDto) {
        log.info("Create Working: {}", workingDto.getName());
        WorkingResponseDto workingCreated = workingService.create(workingDto);
        log.info("Create Working: {}", workingCreated);
        return workingCreated;
    }

    @PatchMapping("/{id}")
    public WorkingResponseDto update(@PathVariable long id,
                            @RequestBody WorkingRequestDto requestDto) {
        log.info("Update Working: {}", id);
        WorkingResponseDto workingUpdated = workingService.update(id, requestDto);
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