package selecting.platform.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import selecting.platform.dto.user.UserProfileResponseDto;
import selecting.platform.security.CustomUserDetails;
import selecting.platform.service.UserService;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        try {
            model.addAttribute("user", UserProfileResponseDto.from(user.getUser()));
            return "user/profile";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PatchMapping("/profile/image")
    public String updateProfileImage(@AuthenticationPrincipal CustomUserDetails user,
                                     @RequestBody Map<String, String> requestBody) {
        String profileImage = requestBody.get("profileImage");
        userService.updateProfileImage(user, profileImage);
        return "redirect:/user/profile";
    }
}
