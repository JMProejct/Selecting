package selecting.platform.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import selecting.platform.error.ErrorCode;
import selecting.platform.error.exception.CustomException;
import selecting.platform.model.User;
import selecting.platform.repository.UserRepository;
import selecting.platform.service.AuthService;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    // 회원가입 페이지로 이동
    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

    // 로그인 페이지로 이동
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @PostMapping("/login")
    public void login(@RequestParam("usernameOrEmail") String usernameOrEmail,
                      @RequestParam("password") String password,
                      HttpServletResponse response) throws IOException {
        authService.login(usernameOrEmail, password, response);
    }

    @PostMapping("/join")
    public String join(@ModelAttribute User user, Model model) {
        try{
            authService.registerUser(user);
            return "redirect:/login";
        } catch (CustomException e) {
            model.addAttribute("error", e.getErrorCode());
            return "join";
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        return authService.logout(response);
    }


    // 아이디 중복 확인
    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam("username") String username) {
        if (userRepository.existsByUsername(username)) {
            throw new CustomException(ErrorCode.ID_ALREADY_EXISTS);
        }

        return ResponseEntity.ok().build();
    }

    // 이메일 중복 확인
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        return ResponseEntity.ok().build();
    }
}
