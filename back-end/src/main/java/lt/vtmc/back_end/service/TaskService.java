package lt.vtmc.back_end.service;

import lt.vtmc.back_end.domain.Project;
import lt.vtmc.back_end.domain.Task;
import lt.vtmc.back_end.domain.TaskPriority;
import lt.vtmc.back_end.domain.TaskStatus;
import lt.vtmc.back_end.model.ReturnTask;
import lt.vtmc.back_end.model.TaskDTO;
import lt.vtmc.back_end.repos.ProjectRepository;
import lt.vtmc.back_end.repos.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class TaskService {
	
	private final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TaskService(final TaskRepository taskRepository,
            final ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }
    
    public List<ReturnTask> getAll() {
    	log.trace("Entering method getAll");
    	log.info("Returning all tasks");
    	return taskRepository.findAll()
                .stream().map(task -> mapToReturnTask(task, new ReturnTask()))
                .collect(Collectors.toList());
    }

    public List<ReturnTask> findAllToCsv(final Long id) {
        log.trace("Entering method findAllToCsv");
        log.debug("Checking if project with id: " + id + " exists");
        final Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        log.info("Returning all task for project with id: " + id);

        return taskRepository.findByProjectTask(project).stream()
                .map(task -> mapToReturnTask(task, new ReturnTask())).collect(Collectors.toList());
    }

    public ResponseEntity<Map<String, Object>> findAllByProject(final Long id, String name) {
        Map<String, Object> response = new HashMap<>();
        log.trace("Entering method findAllByProject");
        log.debug("Checking if project with id: " + id + " exists");
        final Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        response.put("project", project.getName());
        log.info("Returning all tasks for project with id: " + id);
        if(name == null || name.isBlank() || name.length() < 2){
            List<Task> tasks = taskRepository.findByProjectTask(project);
            response.put("tasks", tasks);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if(name.matches("^[+-]?\\d+$")){
            List<Task> tasks = taskRepository.findByProjectTask(project).stream()
                    .filter(task -> task.getId().toString().contains(name)).collect(Collectors.toList());
            response.put("tasks", tasks);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        List<Task> tasks = taskRepository.findByProjectTask(project).stream()
                .filter(task -> task.getName().toLowerCase().contains(name)).collect(Collectors.toList());
        response.put("tasks", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    public Task get(final Long id) {
    	log.trace("Entering method get");
    	log.debug("Checking if task with id: " + id + " exists");
    	log.info("Returning task with id: " + id);
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final Long projectId,final TaskDTO taskDTO) {
    	log.trace("Entering method create");
        if(taskDTO.getName().isBlank()){
            log.error("Name value is null or blank");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    	log.debug("Creating task");
        final Task task = new Task();
        task.setName(taskDTO.getName());
        task.setUserStory(taskDTO.getUserStory());
        task.setStatus(TaskStatus.TO_DO.toString());
        task.setCreationDate(LocalDateTime.now().format(dateFormat));
        task.setUpdateDate(LocalDateTime.now().format(dateFormat));
        
        if(taskDTO.getPriority().isBlank()) {
        	log.error("Priority value is null or blank");
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    	}
        if(taskDTO.getPriority().equals(TaskPriority.LOW.toString())) {
        	task.setPriority(TaskPriority.LOW.toString());
        }
        if(taskDTO.getPriority().equals(TaskPriority.MEDIUM.toString())) {
        	task.setPriority(TaskPriority.MEDIUM.toString());
        }
        if(taskDTO.getPriority().equals(TaskPriority.HIGH.toString())) {
        	task.setPriority(TaskPriority.HIGH.toString());
        }
        log.debug("Adding task to project with id: " + projectId);
        Project project = projectRepository.findById(projectId)
        		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        task.setProjectTask(project);
        
    	taskRepository.save(task);
    	
    	Long prId = task.getProjectTask().getId();
    	updateCount(prId);
    	log.info("task created successfully");
        log.info("Returning task with id: " + task.getId());
        return task.getId();
    }
    
    public void updateStatus(final Long id, final String status) {
    	log.trace("Entering method updateStatus");
    	log.debug("Checking if task with id: " + id + " exists");
    	final Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    	
    	if(status.isBlank()) {
    		log.error("Status value is null or blank");
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    	}
    	if(status.equals(TaskStatus.TO_DO.toString())) {
            log.info("Status updated to TO_DO");
    		task.setStatus(TaskStatus.TO_DO.toString());
    	}
    	if(status.equals(TaskStatus.IN_PROGRESS.toString())) {
            log.info("Status updated to IN_PROGRESS");
    		task.setStatus(TaskStatus.IN_PROGRESS.toString());
    	}
    	if(status.equals(TaskStatus.DONE.toString())) {
            log.info("Status updated to DONE");
    		task.setStatus(TaskStatus.DONE.toString());
    	}
    	
    	taskRepository.save(task);
        log.info("Status updated successfully for task with id: " + id);
    	
    	Long prId = task.getProjectTask().getId();
    	updateCount(prId);
    }

    public void update(final Long id, final TaskDTO taskDTO) {
    	log.trace("Entering method update");
        if(taskDTO.getName().isBlank()){
            log.error("Name value is null or blank");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        log.debug("Checking if task with id: " + id + " exists");
        final Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        task.setName(taskDTO.getName());
        if(taskDTO.getPriority().isBlank()) {
            log.error("Priority value is null or blank");
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    	}
        if(taskDTO.getPriority().equals(TaskPriority.LOW.toString())) {
        	task.setPriority(TaskPriority.LOW.toString());
        }
        if(taskDTO.getPriority().equals(TaskPriority.MEDIUM.toString())) {
        	task.setPriority(TaskPriority.MEDIUM.toString());
        }
        if(taskDTO.getPriority().equals(TaskPriority.HIGH.toString())) {
        	task.setPriority(TaskPriority.HIGH.toString());
        }
        
        task.setUserStory(taskDTO.getUserStory());
        task.setUpdateDate(LocalDateTime.now().format(dateFormat));
        taskRepository.save(task);
        log.info("Task with id: " + id + " updated successfully");
    }

    public void delete(final Long id) {
    	log.trace("Entering method delete");
    	final Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        taskRepository.deleteById(id);
        log.info("Task with id: " + id + " deleted successfully");
        
        Long prId = task.getProjectTask().getId();
        updateCount(prId);
    }
    
    private void updateCount(final Long id) {
    	Project project = projectRepository.findById(id)
        		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    	project.setTasksAmount(project.getProjectTaskTasks().size());
        project.setTasksLeft((int)project.getProjectTaskTasks().stream()
        		.filter(t -> !t.getStatus().equals(TaskStatus.DONE.toString())).count());
        projectRepository.save(project);
    }

    private ReturnTask mapToReturnTask(final Task task, final ReturnTask returnTask) {
        returnTask.setId(task.getId());
        returnTask.setName(task.getName());
        returnTask.setUserStory(task.getUserStory());
        returnTask.setStatus(task.getStatus());
        returnTask.setPriority(task.getPriority());
        returnTask.setCreationDate(task.getCreationDate());
        returnTask.setUpdateDate(task.getUpdateDate());
        return returnTask;
    }


}
