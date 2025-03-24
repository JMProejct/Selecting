package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selecting.platform.model.ServicePost;

public interface ServicePostRepository extends JpaRepository<ServicePost, Integer> {
}
