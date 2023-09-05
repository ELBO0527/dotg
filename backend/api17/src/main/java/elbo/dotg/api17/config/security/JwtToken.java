package elbo.dotg.api17.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtToken {
    private String generateType;
    private String accessToken;
    private String refreshToken;
}
