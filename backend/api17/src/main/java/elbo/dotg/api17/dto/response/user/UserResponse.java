package elbo.dotg.api17.dto.response.user;

import elbo.dotg.api17.domain.User.Role;
import elbo.dotg.api17.domain.User.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String passwd;
    private String name;
    private Role role;

    public UserResponse(final User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.passwd = user.getPasswd();
        this.name = user.getName();
        this.role= user.getRole();
    }
}
