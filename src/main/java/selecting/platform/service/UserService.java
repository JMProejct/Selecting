package selecting.platform.service;

import org.springframework.stereotype.Service;
import selecting.platform.error.ErrorCode;
import selecting.platform.jwt.JWTUtil;
import selecting.platform.model.User;
import selecting.platform.repository.UserRepository;
import selecting.platform.security.CustomUserDetails;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final JWTUtil jwtUtil;

    public UserService(UserRepository userRepository, CustomUserDetailsService customUserDetailsService, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElse(null); // 없으면 null 반환
    }

    public User getUserProfile(String token) {
        if(token == null) {
            throw new IllegalArgumentException(ErrorCode.AUTH_MISSING_TOKEN.getMessage());
        }
        if(jwtUtil.isExpired(token)) {
            throw new IllegalArgumentException(ErrorCode.AUTH_EXPIRED_TOKEN.getMessage());
        }
        String username = jwtUtil.getUsername(token);
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
        return userDetails.getUser();
    }

    public String getUserRole(String token) {
        if(token == null) {
            throw new IllegalArgumentException(ErrorCode.AUTH_MISSING_TOKEN.getMessage());
        }
        if(jwtUtil.isExpired(token)) {
            throw new IllegalArgumentException(ErrorCode.AUTH_EXPIRED_TOKEN.getMessage());
        }
        return jwtUtil.getRole(token);
    }
}
