package lt.vtmc.back_end.repos;

import lt.vtmc.back_end.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByNameIgnoreCaseContaining(String name, Pageable pageable);
}
