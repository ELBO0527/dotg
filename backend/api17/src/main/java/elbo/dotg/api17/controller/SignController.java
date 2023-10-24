package elbo.dotg.api17.controller;

import elbo.dotg.api17.config.security.JwtToken;
import elbo.dotg.api17.dto.request.user.SignUpRequest;
import elbo.dotg.api17.dto.request.user.SigninRequest;
import elbo.dotg.api17.dto.response.common.ApiResponse;
import elbo.dotg.api17.service.user.SignService;
import elbo.dotg.api17.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static elbo.dotg.api17.dto.response.common.ApiResponse.success;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class SignController {

    private final SignService signService;
    private final UserService userService;

    @PostMapping(value = "/signin")
    public ApiResponse<JwtToken> signIn(@RequestBody final SigninRequest signinRequest){
        return success(signService.signin(signinRequest));
    }

    @PostMapping(value = "/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<String> signUp(@RequestBody final SignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
        return success(" 아이디가 생성되었음을 알림");
    }
}
