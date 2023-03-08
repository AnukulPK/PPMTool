package com.example.ppmtool.services;

import com.example.ppmtool.domain.Backlog;
import com.example.ppmtool.domain.Project;
import com.example.ppmtool.domain.ProjectTask;
import com.example.ppmtool.exceptions.ProjectNotFoundException;
import com.example.ppmtool.repositories.BacklogRepository;
import com.example.ppmtool.repositories.ProjectRepository;
import com.example.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;


    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
       try{

           //All Project tasks to be added to a specific project, project!=null
           Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
           //Set the backlog to the project task
           projectTask.setBacklog(backlog);
           //We want the Project Sequence to be = project Identifier-Id of the task within the project = IDPRO-1
           Integer BacklogSequence = backlog.getPTSequence();
           //Update the backlog sequence
           BacklogSequence++;
           backlog.setPTSequence(BacklogSequence);
           //Add Sequence to project task
           projectTask.setProjectSequence(projectIdentifier + "-" + BacklogSequence);
           projectTask.setProjectIdentifier(projectIdentifier);
           // Set an initial priority when priority is null
//        if(projectTask.getPriority()==0||projectTask.getPriority()==null){
//            projectTask.setPriority(3);
//        }
           if (projectTask.getPriority()==0 || projectTask.getPriority() == null) {
               //In the future we need projectTask.getPriority()==0 to handle the form
               projectTask.setPriority(3);
           }
           //Set initial status when status is null
           if (projectTask.getStatus() == null || projectTask.getStatus() == "") {
               projectTask.setStatus("TO_DO");
           }

           return projectTaskRepository.save(projectTask);
       }catch (Exception e){
           throw new ProjectNotFoundException("Project Not Found");
       }

    }

    public Iterable<ProjectTask> findBacklogById(String id) {

        Project project = projectRepository.findByProjectIdentifier(id);

        if(project==null){
            throw new ProjectNotFoundException("Project with id: '"+id+"' does not exist");
        }

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id){

        //make sure we are searching on the right backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if(backlog==null){
            throw new ProjectNotFoundException("Project with id: '"+backlog_id+"' does not exist");
        }

        //make sure that our tasks exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if(projectTask==null){
            throw new ProjectNotFoundException("Project Task with id: '"+pt_id+"' does not exist");
        }

        //make sure that the backlog/project id in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project task '"+pt_id+"' does not exist in project : '"+backlog_id);
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id){
        //find existing project task
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
        //replace it with updated task
        projectTask = updatedTask;
        //save update
        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
        projectTaskRepository.delete(projectTask);
    }
}
