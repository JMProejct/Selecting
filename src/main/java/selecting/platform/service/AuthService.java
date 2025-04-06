package selecting.platform.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import selecting.platform.jwt.JWTUtil;
import selecting.platform.model.Enum.ProviderType;
import selecting.platform.model.Enum.Role;
import selecting.platform.model.User;

import java.io.IOException;
import java.sql.Timestamp;

@Slf4j
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public void login(String usernameOrEmail, String password, HttpServletResponse response) throws IOException {
        try {
            User user = userService.findByUsernameOrEmail(usernameOrEmail);

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("존재하지 않는 사용자입니다.");
                return;
            }

            String username = user.getUsername();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = jwtUtil.createJwt(username, "NORMAL", 60 * 60 * 60L);

            response.addCookie(createCookie("Authorization", token));
            response.sendRedirect("/main");
        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("로그인 실패");
        }
    }

    public ResponseEntity<?> registerUser(User user) {
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }

        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다.");
        }

        String encryptedPassword = passwordEncoder.encode(user.getPassword());

        User newUser = User.builder()
                .email(user.getEmail())
                .name(user.getName())
                .username(user.getUsername())
                .password(encryptedPassword)
                .profileImage(user.getProfileImage())
                .providerType(ProviderType.LOCAL)
                .role(Role.NORMAL)
                .build();

        userService.save(newUser);
        return ResponseEntity.ok("회원가입 성공: " + user.getUsername());
    }

    public ResponseEntity<?> logout(HttpServletResponse response) {
        log.info("로그아웃 실행됨");
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
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
