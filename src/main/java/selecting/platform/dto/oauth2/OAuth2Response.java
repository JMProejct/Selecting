package selecting.platform.dto.oauth2;

public interface OAuth2Response {

    // 플랫폼 : kakao, google
    String getProvider();

    // 플랫폼에서 발급해주는 아이디
    String getProviderId();

    String getEmail();
    String getName();
}
