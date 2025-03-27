package selecting.platform.dto;

import lombok.Data;
import selecting.platform.model.Enum.Role;

@Data
public class UserDto {
    private Role role;
    private String name;
    private String username;
}
