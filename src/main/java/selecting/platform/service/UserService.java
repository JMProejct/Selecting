package selecting.platform.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import selecting.platform.event.UserProfileUpdateEvent;
import selecting.platform.model.User;
import selecting.platform.repository.UserRepository;
import selecting.platform.security.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

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

    @Transactional
    public void updateProfileImage(CustomUserDetails userDetail, String profileImage) {
        User user = userDetail.getUser();
        user.setProfileImage(profileImage);
        userRepository.save(user);

        eventPublisher.publishEvent(new UserProfileUpdateEvent(user.getUserId(), profileImage));
    }
}
