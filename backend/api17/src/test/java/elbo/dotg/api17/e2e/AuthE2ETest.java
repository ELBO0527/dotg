package elbo.dotg.api17.e2e;

import elbo.dotg.api17.dto.request.user.SignUpRequest;
import elbo.dotg.api17.dto.request.user.SigninRequest;
import elbo.dotg.api17.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 인증 관련 E2E 테스트
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1";
    }

    @AfterEach
    void tearDown() {
        userRepository.findByUsername("auth_test_user")
                .ifPresent(user -> userRepository.delete(user));
    }

    @Test
    @DisplayName("회원가입 성공")
    void 회원가입_성공() {
        // given
        SignUpRequest request = new SignUpRequest("auth_test_user", "password123!", "테스트유저");

        // when
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/signup",
                request,
                Map.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().get("status")).isEqualTo("SUCCESS");
        assertThat(userRepository.findByUsername("auth_test_user")).isPresent();
    }

    @Test
    @DisplayName("중복 아이디로 회원가입 실패")
    void 중복_아이디_회원가입_실패() {
        // given - 먼저 회원가입
        SignUpRequest request1 = new SignUpRequest("auth_test_user", "password123!", "첫번째유저");
        restTemplate.postForEntity(baseUrl + "/signup", request1, Map.class);

        // when - 같은 아이디로 다시 회원가입
        SignUpRequest request2 = new SignUpRequest("auth_test_user", "different123!", "두번째유저");
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/signup",
                request2,
                Map.class
        );

        // then
        assertThat(response.getBody().get("status")).isEqualTo("ERROR");
    }

    @Test
    @DisplayName("로그인 성공 - JWT 토큰 발급")
    void 로그인_성공() {
        // given - 회원가입 먼저
        SignUpRequest signUpRequest = new SignUpRequest("auth_test_user", "password123!", "테스트유저");
        restTemplate.postForEntity(baseUrl + "/signup", signUpRequest, Map.class);

        SigninRequest signinRequest = new SigninRequest("auth_test_user", "password123!");

        // when
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/signin",
                signinRequest,
                Map.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("status")).isEqualTo("SUCCESS");

        Map<String, String> tokenData = (Map<String, String>) response.getBody().get("data");
        assertThat(tokenData.get("accessToken")).isNotBlank();
        assertThat(tokenData.get("refreshToken")).isNotBlank();
        assertThat(tokenData.get("grantType")).isEqualTo("Bearer");
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 실패")
    void 잘못된_비밀번호_로그인_실패() {
        // given - 회원가입 먼저
        SignUpRequest signUpRequest = new SignUpRequest("auth_test_user", "password123!", "테스트유저");
        restTemplate.postForEntity(baseUrl + "/signup", signUpRequest, Map.class);

        SigninRequest signinRequest = new SigninRequest("auth_test_user", "wrongPassword!");

        // when
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/signin",
                signinRequest,
                Map.class
        );

        // then
        assertThat(response.getBody().get("status")).isEqualTo("ERROR");
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 로그인 실패")
    void 존재하지_않는_사용자_로그인_실패() {
        // given
        SigninRequest signinRequest = new SigninRequest("nonexistent_user", "password123!");

        // when
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/signin",
                signinRequest,
                Map.class
        );

        // then
        assertThat(response.getBody().get("status")).isEqualTo("ERROR");
    }
}
