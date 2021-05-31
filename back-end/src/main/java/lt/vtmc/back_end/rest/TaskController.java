package lt.vtmc.back_end.rest;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import io.swagger.annotations.ApiOperation;
import lt.vtmc.back_end.domain.Task;
import lt.vtmc.back_end.model.ReturnTask;
import lt.vtmc.back_end.model.TaskDTO;
import lt.vtmc.back_end.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {

    private final Logger log = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(final TaskService taskService) {
        this.taskService = taskService;
    }

    public String currentUserName() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            final String currentPrincipalName = authentication.getName();
            return currentPrincipalName;
        }
        return "no user";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable final Long id) {
        return ResponseEntity.ok(taskService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable final Long id,
            @RequestBody @Valid final TaskDTO taskDTO) {
        log.info("User: " + currentUserName());
        taskService.update(id, taskDTO);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/status")
    @ApiOperation(value="updateStatus", notes="Statuses: TO_DO, IN_PROGRESS, DONE")
    public ResponseEntity<Void> updateTaskStatus(@PathVariable final Long id, 
    		@RequestParam final String status) {
        log.info("User: " + currentUserName());
        taskService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable final Long id) {
        log.info("User: " + currentUserName());
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export/{id}")
    public void exportCSV(HttpServletResponse response, @PathVariable final Long id) throws Exception {
        String filename = "Tasks.csv";

        response.setContentType("txt/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        StatefulBeanToCsv<ReturnTask> writer = new StatefulBeanToCsvBuilder<ReturnTask>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        writer.write(taskService.findAllToCsv(id));
    }

}
