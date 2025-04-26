package selecting.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import selecting.platform.dto.servicepost.ServicePostDetailDto;
import selecting.platform.dto.servicepost.ServicePostResponseDto;
import selecting.platform.dto.teacher.TeacherCardResponseDto;
import selecting.platform.error.ErrorCode;
import selecting.platform.error.exception.CustomException;
import selecting.platform.model.Enum.Role;
import selecting.platform.model.ServicePost;
import selecting.platform.model.User;
import selecting.platform.repository.ServicePostRepository;
import selecting.platform.repository.SubCategoryRepository;
import selecting.platform.repository.UserRepository;

import java.math.BigDecimal;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ServicePostService {

    private final ServicePostRepository servicePostRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final UserRepository userRepository;

    // 포스트 검색
    public Page<ServicePostResponseDto> searchPosts(String keyword, BigDecimal minPrice, BigDecimal maxPrice, String location, Integer minCareer, String education, Pageable pageable) {
        return servicePostRepository.searchWithFilters(keyword, minPrice, maxPrice, location, minCareer, education, pageable);
    }


    // 포스트 상세보기 조회
    public ServicePostDetailDto getPostDetail(Integer postId) {
        ServicePost post = servicePostRepository.findDetailById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // TUTOR 여부 || teacherProfile 존재 여부 확인
        if (post.getUser().getRole() != Role.TUTOR || post.getUser().getTeacherProfile() == null) {
            throw new CustomException(ErrorCode.USER_INVALID_ROLE);
        }


        return ServicePostDetailDto.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .description(post.getDescription())
                .location(post.getLocation())
                .price(post.getPrice())
                .subcategoryName(post.getSubcategory().getSubcategoryName())
                .teacherName(post.getUser().getName())
                .profileImage(post.getUser().getProfileImage())
                .education(post.getUser().getTeacherProfile().getEducation())
                .careerYears(post.getUser().getTeacherProfile().getCareerYears())
                .certifications(post.getUser().getTeacherProfile().getCertifications())
                .intro(post.getUser().getTeacherProfile().getIntro())
                .build();
    }
    
    
    // 선생 카드 조회
    @Transactional
    public Page<TeacherCardResponseDto> getTeacherCards(String subcategoryName, Pageable pageable) {

        // 1. 튜터 검색
        Page<Integer> tutorIdPage;
        if (subcategoryName == null || subcategoryName.isBlank()) {
            String randomSubcategory = subCategoryRepository.getRandomSubCategoryName();
            tutorIdPage = servicePostRepository.findTutorIdsBySubcategory(randomSubcategory, pageable);
        } else {
            tutorIdPage = servicePostRepository.findTutorIdsBySubcategory(subcategoryName, pageable);
        }

        List<Integer> tutorIds = tutorIdPage.getContent();

        if (tutorIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        // 2. 튜터 정보 조회
        List<User> tutors = userRepository.findAllById(tutorIds);

        // 3. 튜터별 카테고리 매핑
        Map<Integer, List<String>> categoryMap = new HashMap<>();
        List<Object[]> rawCategoryList = servicePostRepository.findCategoriesGroupedByTutorIds(tutorIds);
        for (Object[] row : rawCategoryList) {
            Integer userId = (Integer) row[0];
            String subCategoryName = (String) row[1];
            categoryMap.computeIfAbsent(userId, k -> new ArrayList<>()).add(subCategoryName);
        }

        // 4. DTO 조립
        List<TeacherCardResponseDto> results = tutors.stream()
                .map(user -> TeacherCardResponseDto.builder()
                        .teacherId(user.getUserId())
                        .name(user.getName())
                        .profileImage(user.getProfileImage())
                        .categories(categoryMap.getOrDefault(user.getUserId(), List.of()))
                        .intro(user.getTeacherProfile() != null ? user.getTeacherProfile().getIntro() : "")
                        .careerYears(user.getTeacherProfile() != null ? user.getTeacherProfile().getCareerYears() : 0)
                        .rating(0.0)  // 추후 구현
                        .reviewCount(0)  // 추후 구현
                        .build())
                .toList();

        // 5. 페이징 반환
        return new PageImpl<>(results, pageable, tutorIdPage.getTotalElements());
    }

}
