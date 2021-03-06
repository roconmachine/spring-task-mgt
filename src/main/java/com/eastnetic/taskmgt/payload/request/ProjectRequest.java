package com.eastnetic.taskmgt.payload.request;

import javax.validation.constraints.NotBlank;


public class ProjectRequest {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
