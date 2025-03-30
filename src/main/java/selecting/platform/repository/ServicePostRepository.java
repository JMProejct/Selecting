package selecting.platform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import selecting.platform.dto.ServicePostResponseDto;
import selecting.platform.model.ServicePost;
import java.math.BigDecimal;

public interface ServicePostRepository extends JpaRepository<ServicePost, Integer> {
    // 게시글 제목 / 수업 위치 / 과목명 / 교사이름 검색 쿼리 (가격, 범위 , 지역 필터링 기능 추가)
    @Query("SELECT new selecting.platform.dto.ServicePostResponseDto(" +
            "sp.postId, sp.title, sp.location, sp.subcategory.subcategoryName, sp.user.name, sp.price) " +
            "FROM ServicePost sp " +
            "WHERE (:keyword IS NULL OR " +
            "       LOWER(sp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "       OR LOWER(sp.subcategory.subcategoryName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "       OR LOWER(sp.user.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:location IS NULL OR LOWER(sp.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:minPrice IS NULL OR sp.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR sp.price <= :maxPrice)")
    Page<ServicePostResponseDto> searchWithFilters(
            @Param("keyword") String keyword,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("location") String location,
            Pageable pageable);
}
