package com.eastnetic.taskmgt.controllers;

import com.eastnetic.taskmgt.models.User;
import com.eastnetic.taskmgt.payload.response.ProjectResponse;
import com.eastnetic.taskmgt.payload.response.TaskResponse;
import com.eastnetic.taskmgt.repository.ProjectRepository;
import com.eastnetic.taskmgt.repository.TaskRepository;
import com.eastnetic.taskmgt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;


public abstract class ApplicationController {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TaskRepository taskRepository;

    @Autowired
    protected ProjectRepository projectRepository;

    protected User getApplicationUser(){
        String currentUserName = null;
        User user;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();

        }

        user = userRepository.findByUsername(currentUserName)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User Not Found with username: ")
                );
        return user;
    }

    protected User filterUser(User user)
    {
        user.setPassword("****");
        return user;
    }

    protected List<User> filterUsers(List<User> users)
    {
        for (int index =0; index < users.size(); index++)
        users.get(index).setPassword("****");
        return users;
    }

    protected TaskResponse filterTaskResponse(TaskResponse taskResponse){
        if (taskResponse.getTask() != null)
            taskResponse.getTask().setUser(filterUser(taskResponse.getTask().getUser()));

        if (taskResponse.getTasks() != null && taskResponse.getTasks().size() > 0)
        {
            for (int index =0; index < taskResponse.getTasks().size(); index++){
                taskResponse.getTasks().get(index).setUser(filterUser(taskResponse.getTasks().get(index).getUser()));
                taskResponse.getTasks().get(index).getProject().setUser(filterUser(taskResponse.getTasks().get(index).getProject().getUser()));
            }

        }

        return taskResponse;
    }

    protected ProjectResponse filterProjectResponse(ProjectResponse projectResponse){
        if (projectResponse.getListProjects() != null && projectResponse.getListProjects().size() > 0)
        {
            for (int index =0; index < projectResponse.getListProjects().size(); index++)
                projectResponse.getListProjects().get(index).setUser(filterUser(projectResponse.getListProjects().get(index).getUser()));
        }

        return projectResponse;
    }

}
