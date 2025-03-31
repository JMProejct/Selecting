package selecting.platform.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import selecting.platform.jwt.JWTUtil;
import selecting.platform.model.Enum.ProviderType;
import selecting.platform.model.Enum.Role;
import selecting.platform.model.User;
import selecting.platform.service.UserService;

import java.sql.Timestamp;

@Controller
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // JWT 생성
            String token = jwtUtil.createJwt(username, "NORMAL", 60 * 60 * 60L);

            // JWT를 쿠키에 저장
            response.addCookie(createCookie("Authorization", token));

            return ResponseEntity.ok("로그인 성공");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }

    // 회원가입 API
    @PostMapping("/join")
    public ResponseEntity<?> join(@ModelAttribute User user) {

        // username 중복 체크
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }

        // email 중복 체크
        if(userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(user.getPassword());

        User newUser = User.builder()
                .email(user.getEmail())
                .name(user.getName())
                .username(user.getUsername())
                .password(encryptedPassword)
                .profileImage(user.getProfileImage())
                .providerType(ProviderType.LOCAL) // 기본: 로컬 회원가입
                .role(Role.NORMAL) // 기본 권한: 일반 사용자
                .createdAt(new Timestamp(System.currentTimeMillis())) // 현재 시간 저장
                .build();

        userService.save(newUser);

        return ResponseEntity.ok("회원가입 성공: " + user.getUsername());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // 쿠키 삭제
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);  // 쿠키 만료
        cookie.setPath("/");   // 쿠키 경로를 전체로 설정
        response.addCookie(cookie);  // 쿠키를 응답에 추가하여 삭제 처리

        return ResponseEntity.ok("로그아웃 성공");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
