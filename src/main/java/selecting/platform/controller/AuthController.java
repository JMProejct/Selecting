package selecting.platform.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import selecting.platform.model.User;
import selecting.platform.service.AuthService;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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
    public ResponseEntity<?> join(@ModelAttribute User user) {
        return authService.registerUser(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        return authService.logout(response);
    }
}
