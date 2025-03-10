package com.act.project.controller;

import com.act.project.model.Project;
import com.act.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    public Project get(@PathVariable Long id) {
        log.info("Get Project by id: {}", id);
        Project project = projectService.get(id);
        log.info("Get Project: {}", project);
        return project;
    }

    @PostMapping
    public Project create(
            @RequestBody Project project) {
        log.info("Create Project: {}", project.getName());
        Project projectCreated = projectService.create(project);
        log.info("Create Project: {}", projectCreated);
        return projectCreated;
    }

    @PatchMapping("/{id}")
    public Project update(@PathVariable long id,
                            @RequestBody Project project) {
        log.info("Update Project: {}", project.getName());
        Project projectUpdated = projectService.update(id, project);
        log.info("Update Project: {}", projectUpdated);
        return projectUpdated;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Delete Project: {}", id);
        projectService.delete(id);
        log.info("Project with id: {} deleted", id);
    }
}
