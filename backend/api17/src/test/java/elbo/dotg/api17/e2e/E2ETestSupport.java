package elbo.dotg.api17.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import elbo.dotg.api17.dto.request.user.SignUpRequest;
import elbo.dotg.api17.dto.request.user.SigninRequest;
import elbo.dotg.api17.repository.board.BoardRepository;
import elbo.dotg.api17.repository.category.CategoryRepository;
import elbo.dotg.api17.repository.product.ProductRepository;
import elbo.dotg.api17.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

/**
 * E2E 테스트 공통 베이스 클래스
 * 
 * 사용법:
 * class MyE2ETest extends E2ETestSupport {
 *     @Test
 *     void 테스트() {
 *         String token = signUpAndSignIn("user", "pass", "name");
 *         // 인증된 요청...
 *     }
 * }
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public abstract class E2ETestSupport {

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected BoardRepository boardRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected ProductRepository productRepository;

    protected String getBaseUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    /**
     * 회원가입 후 로그인하여 JWT 토큰 반환
     */
    protected String signUpAndSignIn(String username, String password, String name) {
        // 회원가입
        SignUpRequest signUpRequest = new SignUpRequest(username, password, name);
        restTemplate.postForEntity(getBaseUrl() + "/signup", signUpRequest, Map.class);

        // 로그인
        SigninRequest signinRequest = new SigninRequest(username, password);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                getBaseUrl() + "/signin",
                signinRequest,
                Map.class
        );

        Map<String, String> tokenData = (Map<String, String>) response.getBody().get("data");
        return tokenData.get("accessToken");
    }

    /**
     * 인증 헤더 생성
     */
    protected HttpHeaders createAuthHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    /**
     * 인증된 GET 요청
     */
    protected <T> ResponseEntity<T> authenticatedGet(String url, String token, Class<T> responseType) {
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
    }

    /**
     * 인증된 POST 요청
     */
    protected <T> ResponseEntity<T> authenticatedPost(String url, Object body, String token, Class<T> responseType) {
        HttpEntity<?> entity = new HttpEntity<>(body, createAuthHeaders(token));
        return restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
    }

    /**
     * 인증된 PUT 요청
     */
    protected <T> ResponseEntity<T> authenticatedPut(String url, Object body, String token, Class<T> responseType) {
        HttpEntity<?> entity = new HttpEntity<>(body, createAuthHeaders(token));
        return restTemplate.exchange(url, HttpMethod.PUT, entity, responseType);
    }

    /**
     * 인증된 DELETE 요청
     */
    protected <T> ResponseEntity<T> authenticatedDelete(String url, String token, Class<T> responseType) {
        HttpEntity<?> entity = new HttpEntity<>(createAuthHeaders(token));
        return restTemplate.exchange(url, HttpMethod.DELETE, entity, responseType);
    }

    /**
     * 응답에서 data 필드 추출
     */
    @SuppressWarnings("unchecked")
    protected <T> T extractData(ResponseEntity<Map> response) {
        return (T) response.getBody().get("data");
    }

    /**
     * 응답 상태 확인
     */
    protected boolean isSuccess(ResponseEntity<Map> response) {
        return "SUCCESS".equals(response.getBody().get("status"));
    }

    /**
     * 테스트 데이터 전체 삭제
     */
    protected void cleanAllData() {
        boardRepository.deleteAll();
        categoryRepository.deleteAll();
        productRepository.deleteAll();
    }
}
