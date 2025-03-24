package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selecting.platform.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
