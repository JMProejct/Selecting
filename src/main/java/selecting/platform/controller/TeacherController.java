package selecting.platform.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import selecting.platform.dto.teacher.TeacherCardResponseDto;
import selecting.platform.service.ServicePostService;


@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final ServicePostService servicePostService;

    @GetMapping
    public ResponseEntity<Page<TeacherCardResponseDto>> getTeachersBySubCategory(
            @RequestParam(name = "subcategoryName", required = false) String subCategoryName,
            @PageableDefault(size = 12) Pageable pageable
    ) {
        Page<TeacherCardResponseDto> result = servicePostService.getTeacherCards(subCategoryName, pageable);
        return ResponseEntity.ok(result);
    }

}
