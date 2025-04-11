package com.act.subobject.controller;

import com.act.subobject.dto.SubObjectRequestDto;
import com.act.subobject.model.SubObject;
import com.act.subobject.service.SubObjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/subobjects")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class SubObjectController {

    private final SubObjectService subObjectService;

    @GetMapping
    public List<SubObject> getAll() {
        return subObjectService.getAll();
    }

    @GetMapping("/{id}")
    public List<SubObject> getAllByProjectId(@PathVariable long id) {
        log.info("Get all SubObjects");
        return subObjectService.getAllByProjectId(id);
    }

    @PostMapping
    public SubObject create(
            @RequestBody SubObjectRequestDto dto) {
        log.info("Create SubObject: {}", dto.getName());
        SubObject subObjectCreated = subObjectService.create(dto);
        log.info("Create SubObject: {}", subObjectCreated);
        return subObjectCreated;
    }

    @PatchMapping("/{id}")
    public SubObject update(@PathVariable long id,
                            @RequestBody SubObject subObject) {
        log.info("Update SubObject: {}", subObject.getName());
        SubObject subObjectUpdated = subObjectService.update(id, subObject);
        log.info("Update SubObject: {}", subObjectUpdated);
        return subObjectUpdated;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Delete SubObject: {}", id);
        subObjectService.delete(id);
        log.info("SubObject with id: {} deleted", id);
    }
}
