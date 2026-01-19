package elbo.dotg.api17.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import elbo.dotg.api17.domain.category.Category;
import elbo.dotg.api17.domain.category.CategoryType;
import elbo.dotg.api17.dto.request.board.SaveBoardRequest;
import elbo.dotg.api17.dto.request.user.SignUpRequest;
import elbo.dotg.api17.dto.request.user.SigninRequest;
import elbo.dotg.api17.repository.board.BoardRepository;
import elbo.dotg.api17.repository.category.CategoryRepository;
import elbo.dotg.api17.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * E2E 테스트 - 실제 서버를 띄우고 HTTP 요청으로 전체 플로우 검증
 * 
 * 특징:
 * - 실제 HTTP 요청/응답
 * - 실제 DB 사용 (H2 또는 테스트용 DB)
 * - 인증 플로우 포함
 * - 전체 레이어 통합 검증
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // 테스트 간 상태 공유
class BoardE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BoardRepository boardRepository;

    private String baseUrl;
    private String jwtToken;
    private Long categoryId;
    private Long createdBoardId;

    // 테스트용 사용자 정보
    private static final String TEST_USERNAME = "e2e_test_user";
    private static final String TEST_PASSWORD = "test1234!";
    private static final String TEST_NAME = "E2E테스터";

    @BeforeAll
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1";
        cleanUpTestData();
    }

    @AfterAll
    void tearDown() {
        cleanUpTestData();
    }

    private void cleanUpTestData() {
        boardRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.findByUsername(TEST_USERNAME)
                .ifPresent(user -> userRepository.delete(user));
    }

    @Test
    @Order(1)
    @DisplayName("1. 회원가입 - 새로운 사용자 등록")
    void 회원가입_성공() {
        // given
        SignUpRequest request = new SignUpRequest(TEST_USERNAME, TEST_PASSWORD, TEST_NAME);

        // when
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/signup",
                request,
                Map.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo("SUCCESS");

        // DB 검증
        assertThat(userRepository.findByUsername(TEST_USERNAME)).isPresent();
    }

    @Test
    @Order(2)
    @DisplayName("2. 로그인 - JWT 토큰 발급")
    void 로그인_성공_JWT_토큰_발급() {
        // given
        SigninRequest request = new SigninRequest(TEST_USERNAME, TEST_PASSWORD);

        // when
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/signin",
                request,
                Map.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo("SUCCESS");

        // JWT 토큰 저장 (다음 테스트에서 사용)
        Map<String, String> data = (Map<String, String>) response.getBody().get("data");
        jwtToken = data.get("accessToken");
        assertThat(jwtToken).isNotBlank();

        System.out.println("발급된 JWT: " + jwtToken.substring(0, 20) + "...");
    }

    @Test
    @Order(3)
    @DisplayName("3. 카테고리 생성 (게시글 작성 전 필요)")
    void 카테고리_생성() {
        // given - 카테고리 직접 저장 (카테고리 API가 인증 필요하므로)
        Category category = Category.of("E2E테스트 카테고리", CategoryType.BOARD_COMMON, null);
        Category saved = categoryRepository.save(category);
        categoryId = saved.getId();

        // then
        assertThat(categoryId).isNotNull();
        System.out.println("생성된 카테고리 ID: " + categoryId);
    }

    @Test
    @Order(4)
    @DisplayName("4. 게시글 작성 - 인증 필요")
    void 게시글_작성_성공() {
        // given
        SaveBoardRequest request = new SaveBoardRequest(
                "E2E 테스트 게시글 제목",
                "E2E 테스트로 작성된 게시글 내용입니다.",
                categoryId,
                List.of("attachment1.png", "attachment2.pdf")
        );

        HttpHeaders headers = createAuthHeaders();
        HttpEntity<SaveBoardRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/boards",
                HttpMethod.POST,
                entity,
                Map.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo("SUCCESS");

        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
        createdBoardId = ((Number) data.get("id")).longValue();
        assertThat(createdBoardId).isNotNull();

        System.out.println("생성된 게시글 ID: " + createdBoardId);
    }

    @Test
    @Order(5)
    @DisplayName("5. 게시글 단건 조회 - 조회수 증가 확인")
    void 게시글_단건_조회_조회수_증가() {
        // given
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // when - 첫 번째 조회
        ResponseEntity<Map> response1 = restTemplate.exchange(
                baseUrl + "/boards/" + createdBoardId,
                HttpMethod.GET,
                entity,
                Map.class
        );

        // then
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Object> data1 = (Map<String, Object>) response1.getBody().get("data");
        int viewCount1 = ((Number) data1.get("viewCount")).intValue();

        // when - 두 번째 조회
        ResponseEntity<Map> response2 = restTemplate.exchange(
                baseUrl + "/boards/" + createdBoardId,
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> data2 = (Map<String, Object>) response2.getBody().get("data");
        int viewCount2 = ((Number) data2.get("viewCount")).intValue();

        // then - 조회수 증가 확인
        assertThat(viewCount2).isEqualTo(viewCount1 + 1);
        System.out.println("조회수 변화: " + viewCount1 + " → " + viewCount2);
    }

    @Test
    @Order(6)
    @DisplayName("6. 게시글 목록 조회")
    void 게시글_목록_조회() {
        // given
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // when
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/boards",
                HttpMethod.GET,
                entity,
                Map.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Map<String, Object>> boards = (List<Map<String, Object>>) response.getBody().get("data");
        assertThat(boards).isNotEmpty();
        assertThat(boards.size()).isGreaterThanOrEqualTo(1);

        System.out.println("조회된 게시글 수: " + boards.size());
    }

    @Test
    @Order(7)
    @DisplayName("7. 게시글 삭제")
    void 게시글_삭제() {
        // given
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // when
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/boards/" + createdBoardId,
                HttpMethod.DELETE,
                entity,
                Map.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // DB 검증 - 실제로 삭제되었는지
        assertThat(boardRepository.findById(createdBoardId)).isEmpty();
        System.out.println("게시글 삭제 완료: " + createdBoardId);
    }

    @Test
    @Order(8)
    @DisplayName("8. 인증 없이 접근 시 실패")
    void 인증없이_게시글_작성_실패() {
        // given - 토큰 없이 요청
        SaveBoardRequest request = new SaveBoardRequest(
                "인증 없는 게시글",
                "이 요청은 실패해야 함",
                categoryId,
                List.of()
        );

        // when
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/boards",
                request,
                Map.class
        );

        // then - 401 또는 403 예상
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        System.out.println("인증 없는 요청 응답 코드: " + response.getStatusCode());
    }

    @Test
    @Order(9)
    @DisplayName("9. 잘못된 토큰으로 접근 시 실패")
    void 잘못된_토큰으로_접근_실패() {
        // given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("invalid.jwt.token");

        SaveBoardRequest request = new SaveBoardRequest(
                "잘못된 토큰 게시글",
                "이 요청은 실패해야 함",
                categoryId,
                List.of()
        );

        HttpEntity<SaveBoardRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/boards",
                HttpMethod.POST,
                entity,
                Map.class
        );

        // then
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        System.out.println("잘못된 토큰 요청 응답 코드: " + response.getStatusCode());
    }

    /**
     * JWT 인증 헤더 생성 헬퍼 메서드
     */
    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtToken);
        return headers;
    }
}
