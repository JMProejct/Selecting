package selecting.platform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import selecting.platform.dto.servicepost.ServicePostResponseDto;
import selecting.platform.model.ServicePost;
import java.math.BigDecimal;
import java.util.Optional;

public interface ServicePostRepository extends JpaRepository<ServicePost, Integer> {
    // 게시글 제목 / 수업 위치 / 과목명 / 교사이름 검색 쿼리 (가격, 범위 , 지역, 교사 경력, 학력 필터링 기능 추가)
    @Query("SELECT new selecting.platform.dto.servicepost.ServicePostResponseDto(" +
            "sp.postId, sp.title, sp.location, sp.subcategory.subcategoryName, u.name, sp.price, " +
            "tp.careerYears, tp.education) " +
            "FROM ServicePost sp " +
            "JOIN sp.user u " +
            "JOIN u.teacherProfile tp " +
            "WHERE (:keyword IS NULL OR " +
            "       LOWER(sp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "    OR LOWER(sp.subcategory.subcategoryName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "    OR LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:location IS NULL OR LOWER(sp.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:minPrice IS NULL OR sp.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR sp.price <= :maxPrice) " +
            "AND (:minCareer IS NULL OR tp.careerYears >= :minCareer) " +
            "AND (:education IS NULL OR LOWER(tp.education) LIKE LOWER(CONCAT('%', :education, '%')))")
    Page<ServicePostResponseDto> searchWithFilters(
            @Param("keyword") String keyword,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("location") String location,
            @Param("minCareer") Integer minCareer,
            @Param("education") String education,
            Pageable pageable);


    // 교사 상세정보 검색 API
    @Query("SELECT sp FROM ServicePost sp " +
            "JOIN FETCH sp.user u " +
            "JOIN FETCH sp.subcategory s " +
            "JOIN FETCH u.teacherProfile tp " +
            "WHERE sp.postId = :postId")
    Optional<ServicePost> findDetailById(@Param("postId") Integer postId);

}
