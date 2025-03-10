package com.act.subobject.controller;

import com.act.subobject.model.SubObject;
import com.act.subobject.service.SubObjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/subobjects")
@RequiredArgsConstructor
@Slf4j
public class SubObjectController {

    private final SubObjectService subObjectService;

    @GetMapping("/{id}")
    public SubObject get(@PathVariable Long id) {
        log.info("Get SubObject by id: {}", id);
        SubObject subObject = subObjectService.get(id);
        log.info("Get SubObject: {}", subObject);
        return subObject;
    }

    @PostMapping
    public SubObject create(
            @RequestBody SubObject subObject) {
        log.info("Create SubObject: {}", subObject.getName());
        SubObject subObjectCreated = subObjectService.create(subObject);
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
