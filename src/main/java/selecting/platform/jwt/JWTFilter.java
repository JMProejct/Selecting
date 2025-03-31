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
import selecting.platform.dto.CustomOAuth2User;
import selecting.platform.dto.UserDto;
import selecting.platform.model.Enum.Role;

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
        if (requestURI.equals("/join")||requestURI.equals("/login")||requestURI.equals("/index")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키들을 가져와서 Authorization key 에 담긴 쿠키를 찾음
        String authorization = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("Authorization")) {
                authorization = cookie.getValue();
            }
        }

        //Authorization 헤더 검증
        if(authorization == null) {

            logger.info("token null");
            filterChain.doFilter(request, response);
            return;
        }

        //토큰
        String token = authorization;

        if (jwtUtil.isExpired(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired");
            return;
        }

        //토큰에서 username 과 role 가져오기
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setRole(Role.valueOf(role));

        // UserDetails 에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
