package com.eastnetic.taskmgt;

import com.eastnetic.taskmgt.controllers.ProjectsController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
//@ComponentScan(basePackageClasses = ProjectsController.class)
public class SpringBootTaskMgt {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTaskMgt.class, args);
	}

}
