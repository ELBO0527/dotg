package elbo.dotg.api17.service.user;

import elbo.dotg.api17.config.security.JwtToken;
import elbo.dotg.api17.config.security.JwtTokenProvider;
import elbo.dotg.api17.dto.request.user.SigninRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SignServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private SignService signService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

       @Test
       void 로그인을_테스트_해보_자() throws Exception {
            // 가상의 입력 데이터를 생성
            SigninRequest signinRequest = new SigninRequest("testuser", "password");

            // Mock Authentication 객체 생성
            Authentication mockAuthentication = Mockito.mock(Authentication.class);

            // Mock 객체에 필요한 동작 설정
            when(authenticationManager.authenticate(any())).thenReturn(mockAuthentication);

            // 가상의 JWT 토큰 값 설정
            String someAccessToken = "mocked-jwt-token";
            String someRefreshToken = "mocked-jwt-token";
            JwtToken jwtToken = new JwtToken("Bearar", someAccessToken, someRefreshToken);

            when(jwtTokenProvider.createToken(mockAuthentication)).thenReturn(jwtToken);

            // 테스트
            JwtToken testJwtToken = signService.signin(signinRequest);

            // 결과 검증
            assertEquals(jwtToken, testJwtToken);
       }


}