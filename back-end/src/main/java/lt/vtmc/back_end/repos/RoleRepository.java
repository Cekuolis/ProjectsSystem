package lt.vtmc.back_end.repos;

import lt.vtmc.back_end.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {
}
