package selecting.platform.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        // Server log
        System.out.println("❗ [AUTH_UNAUTHORIZED] 로그인이 필요한 서비스입니다.");

        // Redirect 할 경우, 에러 파라미터 추가
        response.sendRedirect("/login?error=unauthorized");
    }
}
