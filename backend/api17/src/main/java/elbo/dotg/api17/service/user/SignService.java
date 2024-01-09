package elbo.dotg.api17.service.user;

import elbo.dotg.api17.advice.exception.sign.CustomAuthenticationException;
import elbo.dotg.api17.config.security.JwtToken;
import elbo.dotg.api17.config.security.JwtTokenProvider;
import elbo.dotg.api17.dto.request.user.SigninRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SignService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public JwtToken signin(final SigninRequest signinRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(signinRequest.username(), signinRequest.passwd());
        try {
            Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            return jwtTokenProvider.createToken(authenticate);
        } catch (AuthenticationException e) {
            throw new CustomAuthenticationException();
        }
    }
}