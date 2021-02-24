package com.eastnetic.taskmgt.payload.response;

import com.eastnetic.taskmgt.models.Task;

import java.util.List;

public class TaskResponse extends Response{

    private Task task;
    private List<Task> tasks;

    public TaskResponse() {
        super();
    }
    public TaskResponse(String code, String message) {
        super(code, message);
    }



    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
