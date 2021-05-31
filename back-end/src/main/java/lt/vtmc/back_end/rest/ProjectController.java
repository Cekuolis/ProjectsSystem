package lt.vtmc.back_end.rest;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import io.swagger.annotations.ApiOperation;
import lt.vtmc.back_end.domain.Project;
import lt.vtmc.back_end.domain.Task;
import lt.vtmc.back_end.model.ProjectDTO;
import lt.vtmc.back_end.model.ReturnProject;
import lt.vtmc.back_end.model.TaskDTO;
import lt.vtmc.back_end.repos.TaskRepository;
import lt.vtmc.back_end.service.ProjectService;
import lt.vtmc.back_end.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/projects", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;
    private final TaskRepository taskRepository;

    private final Logger log = LoggerFactory.getLogger(ProjectController.class);

    public ProjectController(final ProjectService projectService,
    		final TaskService taskService, final TaskRepository taskRepository) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    public String currentUserName() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            final String currentPrincipalName = authentication.getName();
            return currentPrincipalName;
        }
        return "no user";
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProjectsPages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "11") int size,
            @RequestParam(required = false) String title) {

        return projectService.getProjectsPages(page, size, title);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable final Long id) {
        return ResponseEntity.ok(projectService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createProject(@RequestBody @Valid ProjectDTO projectDTO) {
        log.info("User: " + currentUserName());
        return new ResponseEntity<>(projectService.create(projectDTO), HttpStatus.CREATED);
    }
    
    @PostMapping("/{id}/tasks")
    @ApiOperation(value="addTask", notes="Priorities: LOW, MEDIUM, HIGH")
    public ResponseEntity<Void> addTask(@PathVariable final Long id,
            @RequestBody @Valid final TaskDTO taskDTO) {
        log.info("User: " + currentUserName());
    	Long taskId = taskService.create(id, taskDTO);
    	Task task = taskRepository.findById(taskId)
    			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        projectService.addTask(id, task);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}/tasks")
    public ResponseEntity<Map<String, Object>> getAllProjectTasks(@PathVariable final Long id,
                                                         @RequestParam(required = false) String name) {
        return taskService.findAllByProject(id, name);
 }
    
    @PutMapping("/{id}/status")
    @ApiOperation(value="updateStatus", notes="Statuses: IN_PROGRESS, DONE")
    public ResponseEntity<Void> updateStatus(@PathVariable final Long id, 
    		@RequestParam final String status) {
        log.info("User: " + currentUserName());
        projectService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProject(@PathVariable final Long id,
            @RequestBody @Valid final ProjectDTO projectDTO) {
        log.info("User: " + currentUserName());
        projectService.update(id, projectDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable final Long id) {
        log.info("User: " + currentUserName());
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/export")
    public void exportCSV(HttpServletResponse response) throws Exception {
    	String filename = "Projects.csv";
    	
    	response.setContentType("txt/csv");
    	response.setHeader(HttpHeaders.CONTENT_DISPOSITION, 
    			"attachment; filename=\"" + filename + "\"");
    	
    	StatefulBeanToCsv<ReturnProject> writer = new StatefulBeanToCsvBuilder<ReturnProject>(response.getWriter())
    			.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
    			.withSeparator(CSVWriter.DEFAULT_SEPARATOR)
    			.withOrderedResults(false)
    			.build();
    	
    	writer.write(projectService.findAll());
    }

}
