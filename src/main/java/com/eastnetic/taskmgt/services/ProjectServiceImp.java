package com.eastnetic.taskmgt.services;

import com.eastnetic.taskmgt.models.Project;
import com.eastnetic.taskmgt.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ProjectServiceImp implements ProjectService{

    @Autowired
    ProjectRepository repository;

    @Override
    public boolean isExist(String projectName) {
        return repository.existsByName(projectName);
    }

    @Override
    public void save(Project project) {
        repository.save(project);
    }

    @Override
    public void delete(Project project) {
        repository.delete(project);
    }

    @Override
    public List<Project> findAll() {
        return  repository.findAll();
    }

    @Override
    public List<Project> findAll(long userId) {
        return repository.findAllByUserId(userId);
    }
}
