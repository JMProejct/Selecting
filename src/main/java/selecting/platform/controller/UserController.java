package selecting.platform.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import selecting.platform.service.UserService;
import selecting.platform.util.AuthUtil;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthUtil authUtil;

    @GetMapping("/profile")
    public String profile(@CookieValue(name = "Authorization", required = false) String token, Model model) {
        try {
            model.addAttribute("user", authUtil.getUserFromToken(token));
            return "user/profile";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
}
