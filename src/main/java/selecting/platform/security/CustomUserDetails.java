package selecting.platform.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import selecting.platform.model.Enum.Role;
import selecting.platform.model.User;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // 🔐 로그인에 사용되는 ID
    @Override
    public String getUsername() {
        return user.getEmail(); // email을 ID로 사용할 경우
    }

    // 🔐 암호화된 비밀번호
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 🔒 사용자 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + user.getRole().name());
    }

    // ✅ 계정이 만료되지 않았는가?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // ✅ 계정이 잠겨있지 않은가?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // ✅ 자격증명이 만료되지 않았는가?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // ✅ 계정이 활성화 되었는가?
    @Override
    public boolean isEnabled() {
        return true;
    }

    // 🔥 필요 시 user 전체 접근 가능
    public User getUser() {
        return this.user;
    }

    public Integer getUserId() {
        return user.getUserId();
    }

    public Role getRole() {
        return user.getRole();
    }

}
