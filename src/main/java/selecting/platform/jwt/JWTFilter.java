package selecting.platform.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import selecting.platform.model.Enum.Role;
import selecting.platform.model.User;
import selecting.platform.security.CustomUserDetails;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // JWT 검증이 필요 없는 경로 예외 처리
        if (requestURI.equals("/join")
                ||requestURI.equals("/login")
                ||requestURI.equals("/index")
                ||requestURI.startsWith("/static/")
                ||requestURI.startsWith("/js/")
                ||requestURI.startsWith("/favicon.ico")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키들을 가져와서 Authorization key 에 담긴 쿠키를 찾음
        String authorization = null;

        // 1. Authorization 헤더에서 Bearer 토큰 추출
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            authorization = authHeader.substring(7);
        }

        // 2. Authorization 쿠키에서 토큰 추출 (헤더에 없을 때만)
        if (authorization == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("Authorization".equals(cookie.getName())) {
                        authorization = cookie.getValue();
                        break;
                    }
                }
            }
        }

        //토큰이 없을 경우 다음 필터로 넘김
        if(authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        //토큰 만료 여부 확인
        String token = authorization;

        if (jwtUtil.isExpired(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired");
            return;
        }

        //토큰에서 username 과 role 가져오기
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        System.out.println("유저 아이디: " + username);
        System.out.println("유저 권한: " + role);

        // 실제 사용자 객체 생성
        User user = new User();
        user.setEmail(username);
        user.setRole(Role.valueOf(role));

        // Spring Security UserDetails로 래핑
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // 시큐리티 인증 객체 생성 및 등록
        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
