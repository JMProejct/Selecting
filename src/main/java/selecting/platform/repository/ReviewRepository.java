package selecting.platform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import selecting.platform.model.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    public List<Review> findByUserUserId(Integer userId);

    public List<Review> findByPostPostId(Integer postId);

    public Page<Review> findByPostPostId(Integer postId, Pageable pageable);
}
