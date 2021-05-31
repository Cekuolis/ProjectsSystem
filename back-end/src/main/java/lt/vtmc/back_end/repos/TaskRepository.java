package lt.vtmc.back_end.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import lt.vtmc.back_end.domain.Project;
import lt.vtmc.back_end.domain.Task;


public interface TaskRepository extends JpaRepository<Task, Long> {
	
	List<Task> findByProjectTask(Project project);
	
}
