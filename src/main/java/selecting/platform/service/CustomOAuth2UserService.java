package selecting.platform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import selecting.platform.dto.*;
import selecting.platform.model.Enum.Role;
import selecting.platform.model.User;
import selecting.platform.repository.UserRepository;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        }
        else if(registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponseDto(oAuth2User.getAttributes());
        }
        else {
            return null;
        }
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        User existData = userRepository.findByUsername(username).orElse(null);

        // 신규 회원인 경우
        if(existData == null) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(oAuth2Response.getProvider());
            user.setName(oAuth2Response.getProvider());
            user.setRole(Role.NORMAL);

            userRepository.save(user);

            UserDto userDto = new UserDto();
            userDto.setUsername(username);
            userDto.setName(oAuth2Response.getName());
            userDto.setRole(Role.NORMAL);

            return new CustomOAuth2User(userDto);
        }
        else {
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
