package com.eastnetic.taskmgt.repository;

import com.eastnetic.taskmgt.models.Project;
import com.eastnetic.taskmgt.models.Status;
import com.eastnetic.taskmgt.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findById(long id);
    List<Task> findAll();
    List<Task> findAllByStatusIs(Status status);
    List<Task> findAllByProjectIdIs(long project);
    List<Task> findAllByDueDateBefore(Date dueDate);
    List<Task> findAllByUserId(long id);

}
