package selecting.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import selecting.platform.model.Subcategory;

public interface SubCategoryRepository extends JpaRepository<Subcategory, Integer> {

    @Query(value = "SELECT subcategory_name FROM subcategory ORDER BY RAND() LIMIT 1", nativeQuery = true)
    String getRandomSubCategoryName();
}
