package lt.vtmc.back_end.service;

import lt.vtmc.back_end.domain.Project;
import lt.vtmc.back_end.domain.ProjectStatus;
import lt.vtmc.back_end.domain.Task;
import lt.vtmc.back_end.model.ProjectDTO;
import lt.vtmc.back_end.model.ReturnProject;
import lt.vtmc.back_end.repos.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProjectService {

	private final Logger log = LoggerFactory.getLogger(ProjectService.class);
	
    private final ProjectRepository projectRepository;

    public ProjectService(final ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    public ResponseEntity<Map<String, Object>> getProjectsPages(int page, int size, String title){
        try {
            List<Project> projects;
            Pageable paging = PageRequest.of(page, size);

            Page<Project> pageProjects;
            if(title == null || title.isBlank() || title.length() < 2) {
                pageProjects = projectRepository.findAll(paging);
            } else {
                pageProjects = projectRepository.findByNameIgnoreCaseContaining(title, paging);
            }

            projects = pageProjects.getContent();
            List<ReturnProject> returnProjects;
            returnProjects = projects
                    .stream().map(pro -> mapToReturnProject(pro, new ReturnProject()))
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("projects", returnProjects);
            response.put("currentPage", pageProjects.getNumber());
            response.put("totalItems", pageProjects.getTotalElements());
            response.put("totalPages", pageProjects.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<ReturnProject> findAll() {
    	log.trace("Entering method findAll");
    	log.info("Returning all projects");
        return projectRepository.findAll()
        		.stream().map(pro -> mapToReturnProject(pro, new ReturnProject()))
        		.collect(Collectors.toList());
    }

    public Project get(final Long id) {
    	log.trace("Entering method get");
    	log.debug("Checking if project with id: " + id + " exists");
    	log.info("Returning project with id: " + id);
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final ProjectDTO projectDTO) {
    	log.trace("Entering method create");
    	log.debug("Checking if name is not blank or null");
    	if(projectDTO.getName().isBlank()){
            log.error("Name value is null or blank");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    	log.debug("Creating project");
        final Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setProjectTaskTasks(new HashSet<Task>());
        project.setStatus(ProjectStatus.IN_PROGRESS.toString());
        project.setTasksAmount(0);
        project.setTasksLeft(0);
        Long id = projectRepository.save(project).getId();
        log.info("Project created successfully");
        log.info("Returning project with id: " + id);
        return id;
    }
    
    public void addTask(final Long id, final Task task) {
    	log.trace("Entering method addTask");
        log.debug("Checking if project with id: " + id + " exists");
    	final Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    	Set<Task> tasks = project.getProjectTaskTasks();
    	tasks.add(task);
    	project.setProjectTaskTasks(tasks);
    	projectRepository.save(project);
    	log.info("Task added successfully with id: " + task.getId());
    }
    
    public void updateStatus(final Long id, final String status) {
    	log.trace("Entering method updateStatus");
        log.debug("Checking if project with id: " + id + " exists");
    	final Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.debug("Checking if status is not blank or null");
    	if(status.isBlank()) {
    		log.error("Status value is null or blank");
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    	}
    	if(status.equals(ProjectStatus.IN_PROGRESS.toString())) {
    	    log.info("Status updated to IN_PROGRESS");
    		project.setStatus(ProjectStatus.IN_PROGRESS.toString());
    	}
    	if(status.equals(ProjectStatus.DONE.toString())) {
            log.info("Status updated to DONE");
    		project.setStatus(ProjectStatus.DONE.toString());
    	}
    	
     projectRepository.save(project);
     log.info("Status updated successfully for project with id: " + id);
    }

    public void update(final Long id, final ProjectDTO projectDTO) {
    	log.trace("Entering method update");
        log.debug("Checking if project with id: " + id + " exists");
        final Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(projectDTO.getName().isBlank()){
            log.error("Name value is null or blank");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        projectRepository.save(project);
        log.info("Project with id: " + id + " updated successfully");
    }

    public void delete(final Long id) {
    	log.trace("Entering method delete");
        projectRepository.deleteById(id);
        log.info("Project with id: " + id + " and its tasks deleted successfully");
    }

    private ReturnProject mapToReturnProject(final Project project, final ReturnProject returnProject) {
    	returnProject.setId(project.getId());
    	returnProject.setName(project.getName());
    	returnProject.setDescription(project.getDescription());
    	returnProject.setStatus(project.getStatus());
    	returnProject.setTasksAmount(project.getTasksAmount());
    	returnProject.setTasksLeft(project.getTasksLeft());
    	return returnProject;
    }
    


}
