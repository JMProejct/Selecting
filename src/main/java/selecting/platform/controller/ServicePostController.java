package selecting.platform.controller;

import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<ServicePostResponseDto>> searchPosts(@RequestParam("q") String keyword) {
        List<ServicePostResponseDto> result = servicePostService.searchPosts(keyword);
        return ResponseEntity.ok(result);
    }
}
