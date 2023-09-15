package elbo.dotg.api17.dto.response.user;

import elbo.dotg.api17.domain.user.Role;
import elbo.dotg.api17.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String name;
    private Role role;

    public UserResponse(final User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.role = user.getRole();
    }

    public static UserResponse from(final User user) {
        return new UserResponse(user.getId(),
                user.getUsername(),
                user.getName(),
                user.getRole());
    }
}
