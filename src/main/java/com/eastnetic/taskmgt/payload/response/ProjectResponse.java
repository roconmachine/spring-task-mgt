package com.eastnetic.taskmgt.payload.response;

import com.eastnetic.taskmgt.models.Project;

import java.util.List;

public class ProjectResponse extends Response{
    List<Project> listProjects;

    public ProjectResponse() {
        super();
    }
    public ProjectResponse(String code, String message) {
        super(code, message);
    }


    public List<Project> getListProjects() {
        return listProjects;
    }

    public void setListProjects(List<Project> listProjects) {
        this.listProjects = listProjects;
    }

}
