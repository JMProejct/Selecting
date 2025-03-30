package selecting.platform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import selecting.platform.dto.ServicePostResponseDto;
import selecting.platform.service.ServicePostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class ServicePostController {
    private final ServicePostService servicePostService;

    @GetMapping("/search")
    public ResponseEntity<Page<ServicePostResponseDto>> searchPosts(@RequestParam("q") String keyword,
                                                                    @PageableDefault(size = 30, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(servicePostService.searchPosts(keyword, pageable));
    }
}
