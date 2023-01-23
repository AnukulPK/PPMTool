package com.example.ppmtool.services;
import com.example.ppmtool.domain.Backlog;
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


    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){
        //All Project tasks to be added to a specific project, project!=null
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
        //Set the backlog to the project task
        projectTask.setBacklog(backlog);
        //We want the Project Sequence to be = project Identifier-Id of the task within the project = IDPRO-1
        Integer BacklogSequence = backlog.getPTSequence();
        //Update the backlog sequence
        BacklogSequence++;
        //Add Sequence to project task
        projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);
        // Set an initial priority when priority is null
//        if(projectTask.getPriority()==0||projectTask.getPriority()==null){
//            projectTask.setPriority(3);
//        }
        //Set initial status when status is null
        if(projectTask.getStatus()==null||projectTask.getStatus()==""){
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);
    }
}
