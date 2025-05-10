package selecting.platform.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import selecting.platform.model.Enum.ProviderType;
import selecting.platform.model.Enum.Role;
import selecting.platform.model.User;

@Getter
@AllArgsConstructor
public class UserProfileResponseDto {
    private String name;
    private String email;
    private String profileImage;
    private ProviderType providerType;
    private Role role;

    public static UserProfileResponseDto from(User user) {
        return new UserProfileResponseDto(
                user.getName(),
                user.getEmail(),
                user.getProfileImage(),
                user.getProviderType(),
                user.getRole()
        );
    }
}
