package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selecting.platform.model.Subcategory;

public interface SubCategoryRepository extends JpaRepository<Subcategory, Integer> {
}
