package elbo.dotg.api17.controller;

import elbo.dotg.api17.config.security.JwtToken;
import elbo.dotg.api17.dto.request.user.SigninRequest;
import elbo.dotg.api17.dto.response.common.ApiResponse;
import elbo.dotg.api17.service.user.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static elbo.dotg.api17.dto.response.common.ApiResponse.success;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class SignController {

    private final SignService signService;

    @PostMapping(value = "/signin")
    public ApiResponse<JwtToken> signin(@RequestBody SigninRequest signinRequest){
        return success(signService.signin(signinRequest));
    }
}
