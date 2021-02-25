package com.eastnetic.taskmgt.controllers;

import com.eastnetic.taskmgt.models.Project;
import com.eastnetic.taskmgt.payload.request.ProjectRequest;
import com.eastnetic.taskmgt.payload.response.ProjectResponse;
import com.eastnetic.taskmgt.payload.response.Response;
import org.apache.log4j.Logger;
import org.graalvm.compiler.lir.StandardOp;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/project")
public class ProjectsController
        extends ApplicationController
{

    private static final Logger logger = Logger.getLogger(ProjectsController.class);

    @PostMapping(value = "/create", produces = "application/json")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Response projectPerform(@Valid @RequestBody ProjectRequest projectRequest) {

        logger.info("/create request : project name : " + projectRequest.getName());
        if (projectRepository.existsByName(projectRequest.getName())) {
            logger.info("Project name already taken !");
            return new Response("1001", "Project name already taken !");
        }

        Project project = null;

        try{
            project = new Project();
            project.setName(projectRequest.getName());
            project.setUser(super.getApplicationUser());
            projectRepository.save(project);
            logger.info("project saved");
        }
        catch (UsernameNotFoundException usernameNotFoundException)
        {
            logger.error(usernameNotFoundException.getMessage());
            return  new Response("1003", usernameNotFoundException.getMessage());
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
            return new Response("9999", "System failure");
        }

        return new Response();
    }


    @PostMapping(value = "/delete/{id}", produces = "application/json")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Response delete(@PathVariable("id") long id, @RequestBody ProjectRequest projectRequest) {
        logger.info("/delete/{id} request data id= " + id + " name=" + projectRequest.getName() );
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
            logger.info("deleted");
        }
        catch (Exception exception){
            logger.error(exception.getMessage());
            return new Response("9999", "System failure");
        }
        return new Response();
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ProjectResponse getAllProjects(){

        logger.info("/all request");
        ProjectResponse projectResponse = new ProjectResponse();
        try {
            projectResponse.setListProjects(projectRepository.findAll());
            logger.info("project found " + projectResponse.getListProjects().size());
        }catch (Exception ex){
            logger.error(ex.getMessage());
            new ProjectResponse("9999", "system failure");
        }


        return super.filterProjectResponse(projectResponse);
    }


    @GetMapping(value = "/getAllProjectsByUser/{userid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProjectResponse getAllProjectsByUser(@PathVariable("userid") long id){

        logger.info("/getAllProjectsByUser request data userid=" + id);
        ProjectResponse response = new ProjectResponse();
        try {
            response.setListProjects(projectRepository.findAllByUserId(id));
            logger.info("Project found " + response.getListProjects().size());
        }catch (Exception exception){
            logger.error(exception.getMessage());
            return new ProjectResponse("9999", "System failure");
        }
        return super.filterProjectResponse(response);

    }
}
