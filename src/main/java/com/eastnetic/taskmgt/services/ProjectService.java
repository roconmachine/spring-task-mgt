package com.eastnetic.taskmgt.services;

import com.eastnetic.taskmgt.models.Project;

import java.util.List;

public interface ProjectService {
    public boolean isExist(String projectName);
    public void save(Project project);
    public void delete(Project project);
    public List<Project> findAll();
    public List<Project> findAll(long userId);


}
