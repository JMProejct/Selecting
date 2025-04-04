package selecting.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import selecting.platform.dto.servicepost.ServicePostDetailDto;
import selecting.platform.dto.servicepost.ServicePostResponseDto;
import selecting.platform.error.ErrorCode;
import selecting.platform.error.exception.CustomException;
import selecting.platform.model.Enum.Role;
import selecting.platform.model.ServicePost;
import selecting.platform.repository.ServicePostRepository;
import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class ServicePostService {

    private final ServicePostRepository servicePostRepository;

    public Page<ServicePostResponseDto> searchPosts(String keyword, BigDecimal minPrice, BigDecimal maxPrice, String location, Integer minCareer, String education, Pageable pageable) {
        return servicePostRepository.searchWithFilters(keyword, minPrice, maxPrice, location, minCareer, education, pageable);
    }


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

}
