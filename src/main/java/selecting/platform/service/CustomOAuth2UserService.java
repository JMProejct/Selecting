package selecting.platform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import selecting.platform.dto.*;
import selecting.platform.dto.oauth2.CustomOAuth2User;
import selecting.platform.dto.oauth2.GoogleResponseDto;
import selecting.platform.dto.oauth2.KakaoResponseDto;
import selecting.platform.dto.oauth2.OAuth2Response;
import selecting.platform.model.Enum.ProviderType;
import selecting.platform.model.Enum.Role;
import selecting.platform.model.User;
import selecting.platform.repository.UserRepository;

import java.sql.Timestamp;
import java.util.UUID;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomOAuth2UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info(oAuth2User.toString());

        // 어떤 플랫폼에서 로그인을 했는지 구분
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if(registrationId.equals("google")) {
            oAuth2Response = new GoogleResponseDto(oAuth2User.getAttributes());
            registrationId = "GOOGLE";
        }
        else if(registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponseDto(oAuth2User.getAttributes());
            registrationId = "KAKAO";
        }
        else {
            return null;
        }
        String email = oAuth2Response.getEmail();
        User existData = userRepository.findByUsername(email).orElse(null);

        // 랜덤 비밀번호 생성
        String randomPassword = UUID.randomUUID().toString();
        // 신규 회원인 경우
        if(existData == null) {
            User user = new User();
            user.setUsername(email);
            user.setEmail(email);
            user.setName(oAuth2Response.getName());
            user.setRole(Role.NORMAL);
            user.setProviderType(ProviderType.valueOf(registrationId));
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            user.setPassword(bCryptPasswordEncoder.encode(randomPassword)); // 비밀번호 암호화 저장

            userRepository.save(user);

            UserDto userDto = new UserDto();
            userDto.setUsername(email);
            userDto.setName(oAuth2Response.getName());
            userDto.setRole(Role.NORMAL);

            return new CustomOAuth2User(userDto);
        }
        // 기존 유저가 있을시 sso 로그인으로 전환
        else {
            if (existData.getProviderType() == ProviderType.LOCAL) {
                // 기존 로컬 회원 → 소셜 로그인 정보 업데이트
                existData.setProviderType(ProviderType.valueOf(registrationId));
                existData.setPassword(bCryptPasswordEncoder.encode(randomPassword)); // 비밀번호 랜덤으로 설정 (소셜 로그인 사용)
            }
            existData.setName(oAuth2Response.getName());
            existData.setEmail(oAuth2Response.getEmail());

            userRepository.save(existData);

            UserDto userDto = new UserDto();
            userDto.setUsername(existData.getUsername());
            userDto.setName(oAuth2Response.getName());
            userDto.setRole(existData.getRole());

            return new CustomOAuth2User(userDto);
        }
    }
}
