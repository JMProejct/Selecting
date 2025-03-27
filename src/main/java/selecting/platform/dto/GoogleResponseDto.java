package selecting.platform.dto;

import java.util.Map;

public class GoogleResponseDto implements OAuth2Response{

    private final Map<String, Object> attribute;

    public GoogleResponseDto(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return "sub";
    }

    @Override
    public String getEmail() {
        return "email";
    }

    @Override
    public String getName() {
        return "name";
    }
}
