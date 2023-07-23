package elbo.dotg.api17.dto.request.user;

public record SignUpRequest(
        String username,
        String passwd,
        String name
) {}
