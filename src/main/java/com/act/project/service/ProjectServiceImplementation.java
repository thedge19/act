package com.act.project.service;

import com.act.exception.exception.NotFoundException;
import com.act.project.model.Project;
import com.act.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImplementation implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public Project get(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Project create(Project project) {
        return projectRepository.save(project);
    }

    @Transactional
    @Override
    public Project update(long id, Project project) {
        Project updatedProject = findProjectOrNot(id);

        if (project.getName() != null) {
            updatedProject.setName(project.getName());
        }

        return projectRepository.save(updatedProject);
    }

    @Transactional
    @Override
    public void delete(long id) {
        findProjectOrNot(id);
        projectRepository.deleteById(id);
    }

    @Override
    public Project findProjectOrNot(long id) {
        return projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Подобъект не найден"));
    }
}