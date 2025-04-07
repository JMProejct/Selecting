package selecting.platform.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import selecting.platform.error.ErrorCode;
import selecting.platform.jwt.JWTUtil;
import selecting.platform.model.User;
import selecting.platform.security.CustomUserDetails;
import selecting.platform.service.CustomUserDetailsService;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public User getUserFromToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException(ErrorCode.AUTH_MISSING_TOKEN.getMessage());
        }
        if (jwtUtil.isExpired(token)) {
            throw new IllegalArgumentException(ErrorCode.AUTH_EXPIRED_TOKEN.getMessage());
        }
        String username = jwtUtil.getUsername(token);
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
        return userDetails.getUser();
    }
}
