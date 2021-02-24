package com.eastnetic.taskmgt.controllers;


import com.eastnetic.taskmgt.models.Project;
import com.eastnetic.taskmgt.models.User;
import com.eastnetic.taskmgt.payload.request.LoginRequest;
import com.eastnetic.taskmgt.payload.request.ProjectRequest;
import com.eastnetic.taskmgt.payload.response.MessageResponse;
import com.eastnetic.taskmgt.payload.response.ProjectResponse;
import com.eastnetic.taskmgt.payload.response.Response;
import com.eastnetic.taskmgt.repository.ProjectRepository;
import com.eastnetic.taskmgt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/project")
public class ProjectsController
//        extends ApplicationController
{

    @Autowired
    ProjectRepository projectRepository;

//    @Autowired
//    UserRepository userRepository;

    @PostMapping(value = "/create", produces = "application/json")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Response projectPerform(@Valid @RequestBody ProjectRequest projectRequest) {

        if (projectRepository.existsByName(projectRequest.getName())) {
            return new Response("1001", "Project name already taken !");
        }

        Project project = null;

        try{
            //User user = super.getApplicationUser();
            project = new Project();
            project.setName(projectRequest.getName());
            //project.setUser(user);
            projectRepository.save(project);

        }
        catch (UsernameNotFoundException usernameNotFoundException)
        {
            return  new Response("1003", usernameNotFoundException.getMessage());
        }
        catch (Exception ex){
            ex.printStackTrace();
            return new Response("9999", "System failure");
        }

        return new Response();
    }


    @PostMapping(value = "/delete/{id}", produces = "application/json")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Response delete(@PathVariable("id") long id, @RequestBody ProjectRequest projectRequest) {

        if ( id < 1 || !projectRepository.existsById(id))
            return new Response("1001" , "Project does not exist");

        try {
            if (projectRequest.getName() != null && !projectRequest.getName().trim().isEmpty()) {
                Project project = new Project();
                project.setId(id);
                project.setName(projectRequest.getName());
                projectRepository.delete(project);
            }
            else
                projectRepository.deleteById(id);
        }
        catch (Exception exception){
            exception.printStackTrace();
            return new Response("9999", exception.getMessage());
        }
        return new Response();
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ProjectResponse getAllProjects(){

        ProjectResponse projectResponse = new ProjectResponse();

        projectResponse.setListProjects(projectRepository.findAll());

        return projectResponse;
    }
}
