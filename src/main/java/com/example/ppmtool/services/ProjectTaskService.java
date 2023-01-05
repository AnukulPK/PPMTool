package com.example.ppmtool.services;
import com.example.ppmtool.domain.ProjectTask;
import com.example.ppmtool.repositories.BacklogRepository;
import com.example.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;


    public ProjectTask addProjectTask(){
        //All Project tasks to be added to a specific project, project!=null

        //Set the backlog to the project task

        //We want the Project Sequence to be = project Identifier-Id of the task within the project = IDPRO-1

        //Update the backlog sequence

        // Set an initial priority when priority is null

        //Set initial status when status is null

    }
}
