package com.act.project.service;

import com.act.project.model.Project;

import java.util.List;

public interface ProjectService {
    Project get(Long id);

    List<Project> getALL();

    Project create(Project project);

    Project update(long id, Project project);

    void delete(long id);

    Project findProjectOrNot(long id);
}
