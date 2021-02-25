package com.eastnetic.taskmgt.controllers;


import com.eastnetic.taskmgt.models.Project;
import com.eastnetic.taskmgt.models.Status;
import com.eastnetic.taskmgt.models.Task;

import com.eastnetic.taskmgt.payload.request.TaskRequest;

import com.eastnetic.taskmgt.payload.response.Response;
import com.eastnetic.taskmgt.payload.response.TaskResponse;

import org.apache.log4j.Logger;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/task")
public class TaskController
        extends ApplicationController
{

    private static final Logger logger = Logger.getLogger(TaskController.class);
    @PostMapping(value = "/create", consumes = "application/json")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Response create(@Valid @RequestBody TaskRequest taskRequest) {

        logger.info("/create request data description=" + taskRequest.getDescription() +
                " status=" + taskRequest.getStatus()
                + " projectid=" + taskRequest.getProjectId()+
                " date=" + taskRequest.getDueDate().toString());
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
            logger.info("saved");

        }
        catch (UsernameNotFoundException exception){
            logger.warn(exception.getMessage());
            return  new Response("1003", exception.getMessage());
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            return  new Response("9999", "System failure");
        }


        return new Response();
    }


    @GetMapping(value = "/get/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public TaskResponse getById(@PathVariable("id") long id){

        logger.info("/get/{id} request data task id=" +id);
        TaskResponse response = new TaskResponse();
        try{
            response.setTask(taskRepository.findById(id));
        }catch (Exception exception) {
            logger.error(exception.getMessage());
            new TaskResponse("9999", "System failure");
        }


        return super.filterTaskResponse(response);
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public TaskResponse getById(){
        logger.info("/all request");
        TaskResponse response = new TaskResponse();
        try{
            response.setTasks(taskRepository.findAll());
            logger.info("task found " + response.getTasks().size());
        }catch (Exception exception) {
            logger.error(exception.getMessage());
            new TaskResponse("9999", "System failure");
        }


        return super.filterTaskResponse(response);
    }

    @GetMapping(value = "/get/pId={project_id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public TaskResponse getByProject(@PathVariable("project_id") long id){

        logger.info("/get/pId={project_id} request data project id=" + id);

        TaskResponse response = new TaskResponse();
        try{
            response.setTasks(taskRepository.findAllByProjectIdIs(id));
            logger.info("task found " + response.getTasks().size());
        }catch (Exception exception) {
            logger.error(exception.getMessage());
            new TaskResponse("9999", "System failure");
        }
        return super.filterTaskResponse(response);
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
        return super.filterTaskResponse(response);
    }

    @GetMapping(value = "/get/dDate")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public TaskResponse getByDueDate(){
        logger.info("/get/dDate request");

        TaskResponse response = new TaskResponse();
        try{
            response.setTasks(taskRepository.findAllByDueDateBefore(Calendar.getInstance().getTime()));
            logger.info("task found " + response.getTasks().size());
        }catch (Exception exception) {
            logger.error(exception.getMessage());
            new TaskResponse("9999", "System failure");
        }
        return super.filterTaskResponse(response);
    }

    @GetMapping(value = "/get/dDateNotClose")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public TaskResponse getByDueDateNotClose(){

        logger.info("get/dDateMotClose request data");
        TaskResponse response = new TaskResponse();
        try{
            response.setTasks(taskRepository.findAllByDueDateBeforeAndStatusNot(Calendar.getInstance().getTime(), Status.CLOSE.toString()));
            logger.info("task found " + response.getTasks().size());
        }catch (Exception exception) {
            logger.error(exception.getMessage());
            new TaskResponse("9999", "System failure");
        }
        return super.filterTaskResponse(response);
    }


    @PostMapping(value = "/edit", consumes = "application/json")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Response edit(@Valid @RequestBody TaskRequest taskRequest) {

        logger.info("/edit request data description=" + taskRequest.getDescription() +
                " status=" + taskRequest.getStatus()
                + " projectid=" + taskRequest.getProjectId()+
                " date=" + taskRequest.getDueDate().toString());
        Status status;
        try
        {
            status = Status.valueOf(taskRequest.getStatus());

        }catch (Exception e){
            logger.warn(e.getMessage());
            return new Response("1001", "Invalid status : " + taskRequest.getStatus());
        }

        Project project = projectRepository.findById(taskRequest.getProjectId());

        if(project == null){
            logger.warn("Invalid project : " + taskRequest.getProjectId());
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
            logger.info("saved");

        }catch (Exception e)
        {
            logger.error(e.getMessage());
            return  new Response("9999", "System failure");
        }


        return new Response();
    }


    @GetMapping(value = "/getAllTaskByUser/{userid}")
    @PreAuthorize("hasRole('ADMIN')")
    public TaskResponse getallTasksByUser(@PathVariable("userid") long userId){

        logger.info("/getAllTaskByUser/{user_id} request data userid=" +userId);
        TaskResponse response = new TaskResponse();
        try {
            response.setTasks(taskRepository.findAllByUserId(userId));
            logger.info("task found " + response.getTasks().size());
        }catch (Exception exception){
            logger.error(exception.getMessage());
           return new TaskResponse("9999", "System failure");
        }
        return super.filterTaskResponse(response);
    }


}
