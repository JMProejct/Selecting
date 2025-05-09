package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selecting.platform.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username,String email);

    boolean existsByUsername(String email);

    boolean existsByEmail(String email);


}
