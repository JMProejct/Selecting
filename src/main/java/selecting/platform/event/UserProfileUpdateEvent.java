package selecting.platform.event;

public class UserProfileUpdateEvent {
    private final Integer userId;
    private final String profileImage;

    public UserProfileUpdateEvent(Integer userId, String profileImage) {
        this.userId = userId;
        this.profileImage = profileImage;
    }

    public Integer getUserId() { return userId; }
    public String getProfileImage() { return profileImage; }
}
