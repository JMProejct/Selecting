package selecting.platform.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import selecting.platform.service.UserService;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(@CookieValue(name = "Authorization", required = false) String token, Model model) {
        try {
            model.addAttribute("user", userService.getUserProfile(token));
            return "user/profile";
        } catch (RuntimeException e) {
            log.error("프로필 조회 실패: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
}
