package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selecting.platform.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
