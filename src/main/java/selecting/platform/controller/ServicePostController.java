package selecting.platform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import selecting.platform.dto.servicepost.ServicePostDetailDto;
import selecting.platform.dto.servicepost.ServicePostResponseDto;
import selecting.platform.model.ServicePost;
import selecting.platform.model.User;
import selecting.platform.service.ServicePostService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class ServicePostController {
    private final ServicePostService servicePostService;


    // 검색 API
    @GetMapping("/search")
    public ResponseEntity<Page<ServicePostResponseDto>> searchPosts(@RequestParam(value = "q", required = false) String keyword,
                                                                    @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
                                                                    @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
                                                                    @RequestParam(value = "location", required = false) String location,
                                                                    @RequestParam(value = "minCareer", required = false) Integer minCareer,
                                                                    @RequestParam(value = "education", required = false) String education,
                                                                    @PageableDefault(size = 30, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(servicePostService.searchPosts(keyword, minPrice, maxPrice, location, minCareer, education, pageable));
    }

    // 교사 상세 정보 검색 API
    @GetMapping("/{postId}")
    public ResponseEntity<ServicePostDetailDto> getPostDetail(@PathVariable Integer postId) {
        return ResponseEntity.ok(servicePostService.getPostDetail(postId));
    }


    // 게시글 목록 API
    @GetMapping("/all")
    public ResponseEntity<Page<ServicePostResponseDto>> getAllPosts(@PageableDefault(size = 50, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ServicePostResponseDto> posts = servicePostService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody ServicePost post, @AuthenticationPrincipal User user) {
        post.setUser(user);
        Integer postId = servicePostService.createPost(post);
        return ResponseEntity.ok(postId);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Integer postId, @RequestBody ServicePost updated, @AuthenticationPrincipal User user) {
        servicePostService.updatePost(postId, updated, user);
        return ResponseEntity.ok("수정 완료");
    }
    
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Integer postId, @AuthenticationPrincipal User user) {
        servicePostService.deletePost(postId, user);
        return ResponseEntity.ok("삭제 완료");
    }


}
