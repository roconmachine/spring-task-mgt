package com.eastnetic.taskmgt.controllers;


import com.eastnetic.taskmgt.models.Project;
import com.eastnetic.taskmgt.models.Status;
import com.eastnetic.taskmgt.models.Task;
import com.eastnetic.taskmgt.models.User;
import com.eastnetic.taskmgt.payload.request.TaskRequest;
import com.eastnetic.taskmgt.payload.response.Response;
import com.eastnetic.taskmgt.payload.response.TaskResponse;
import com.eastnetic.taskmgt.repository.ProjectRepository;
import com.eastnetic.taskmgt.repository.TaskRepository;
import com.eastnetic.taskmgt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/task")
public class TaskController
        extends ApplicationController
{

    @PostMapping(value = "/create", consumes = "application/json")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Response create(@Valid @RequestBody TaskRequest taskRequest) {

        Status status;
        try
        {
            status = Status.valueOf(taskRequest.getStatus());

        }catch (Exception e){
            return new Response("1001", "Invalid status : " + taskRequest.getStatus());
        }

        try{
            Project project = projectRepository.findById(taskRequest.getProjectId());

            taskRepository.save(new Task(taskRequest.getDescription(),
                    Status.valueOf(taskRequest.getStatus()),
                    taskRequest.getDueDate(),
                    project,
                    super.getApplicationUser()
            ));

        }
        catch (UsernameNotFoundException exception){
            return  new Response("1003", exception.getMessage());
        }
        catch (Exception e)
        {
            return  new Response("9999", "System failure");
        }


        return new Response();
    }


    @GetMapping(value = "/get/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public TaskResponse getById(@PathVariable("id") long id){

        TaskResponse response = new TaskResponse();
        try{
            response.setTask(taskRepository.findById(id));
        }catch (Exception exception) {
            new TaskResponse("9999", "System failure");
        }


        return response;
    }

    @GetMapping(value = "/get/pId={project_id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public TaskResponse getByProject(@PathVariable("project_id") long id){


        TaskResponse response = new TaskResponse();
        try{
            response.setTasks(taskRepository.findAllByProjectIdIs(id));
        }catch (Exception exception) {
            new TaskResponse("9999", "System failure");
        }
        return response;
    }

    @GetMapping(value = "/get/status={status}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public TaskResponse getByStatus(@PathVariable("status") String statusValue){

        Status status;
        try
        {
            status = Status.valueOf(statusValue);

        }catch (Exception e){
            return new TaskResponse("1001", "Invalid status : " + statusValue);
        }

        TaskResponse response = new TaskResponse();
        try{
            response.setTasks(taskRepository.findAllByStatusIs(status));
        }catch (Exception exception) {
            new TaskResponse("9999", "System failure");
        }
        return response;
    }

    @GetMapping(value = "/get/dDate")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public TaskResponse getByDueDate(){


        TaskResponse response = new TaskResponse();
        try{
            response.setTasks(taskRepository.findAllByDueDateBefore(Calendar.getInstance().getTime()));
        }catch (Exception exception) {
            new TaskResponse("9999", "System failure");
        }
        return response;
    }


    @PostMapping(value = "/edit", consumes = "application/json")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Response edit(@Valid @RequestBody TaskRequest taskRequest) {

        Status status;
        try
        {
            status = Status.valueOf(taskRequest.getStatus());



        }catch (Exception e){
            return new Response("1001", "Invalid status : " + taskRequest.getStatus());
        }

        Project project = projectRepository.findById(taskRequest.getProjectId());

        if(project == null){
            return new Response("1001", "Invalid project : " + taskRequest.getProjectId());
        }

        try{

            Task task = taskRepository.findById(taskRequest.getId());
            if (task == null) return new Response("1001", "Invalid task : " + taskRequest.getId());
            if (task.getStatus() == Status.CLOSE) return new Response("1002", "You cannot edit closed task");

            task.setDescription(taskRequest.getDescription());
            task.setStatus(Status.valueOf(taskRequest.getStatus()));
            task.setDueDate(taskRequest.getDueDate());

            taskRepository.save(task);

        }catch (Exception e)
        {
            return  new Response("9999", "System failure");
        }


        return new Response();
    }


    @GetMapping(value = "/getAllTaskByUser/{userid}")
    @PreAuthorize("hasRole('ADMIN')")
    public TaskResponse getallTasksByUser(@PathVariable("userid") long userId){

        TaskResponse response = new TaskResponse();
        try {
            response.setTasks(taskRepository.findAllByUserId(userId));
        }catch (Exception exception){
           return new TaskResponse("9999", "System failure");
        }
        return response;
    }





}
