package lt.vtmc.back_end.repos;

import lt.vtmc.back_end.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByMail(String mail);
	
}
