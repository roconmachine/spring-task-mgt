package com.eastnetic.taskmgt.repository;


import com.eastnetic.taskmgt.models.Project;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ProjectRepository extends CrudRepository<Project, Long> {


    boolean existsById(long id);
    boolean existsByName(String name);
    void deleteById(long aLong);
    void delete(Project project);
    List<Project> findAll();
    Project findById(long id);
}
