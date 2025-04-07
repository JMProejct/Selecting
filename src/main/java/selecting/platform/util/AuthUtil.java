package selecting.platform.util;

import org.springframework.stereotype.Component;
import selecting.platform.jwt.JWTUtil;
import selecting.platform.model.User;
import selecting.platform.security.CustomUserDetails;
import selecting.platform.service.CustomUserDetailsService;

@Component
public class AuthUtil {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public AuthUtil(JWTUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    public User getUserFromToken(String token) {
        if (token == null || jwtUtil.isExpired(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        String username = jwtUtil.getUsername(token);
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        return userDetails.getUser();
    }
}
