package elbo.dotg.api17.e2e;

import elbo.dotg.api17.domain.category.CategoryType;
import elbo.dotg.api17.dto.request.category.SaveCategoryRequest;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 카테고리 E2E 테스트 - E2ETestSupport 활용 예제
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryE2ETest extends E2ETestSupport {

    private String jwtToken;
    private Long parentCategoryId;
    private Long childCategoryId;

    private static final String TEST_USER = "category_e2e_user";
    private static final String TEST_PASS = "test1234!";

    @BeforeAll
    void setUpAll() {
        cleanAllData();
        userRepository.findByUsername(TEST_USER)
                .ifPresent(user -> userRepository.delete(user));
        
        // 회원가입 + 로그인
        jwtToken = signUpAndSignIn(TEST_USER, TEST_PASS, "카테고리테스터");
    }

    @AfterAll
    void tearDownAll() {
        cleanAllData();
        userRepository.findByUsername(TEST_USER)
                .ifPresent(user -> userRepository.delete(user));
    }

    @Test
    @Order(1)
    @DisplayName("1. 부모 카테고리 생성")
    void 부모_카테고리_생성() {
        // given
        SaveCategoryRequest request = new SaveCategoryRequest(
                "개발 게시판",
                CategoryType.BOARD_COMMON,
                null  // 부모 없음
        );

        // when
        ResponseEntity<Map> response = authenticatedPost(
                getBaseUrl() + "/categories",
                request,
                jwtToken,
                Map.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(isSuccess(response)).isTrue();

        Map<String, Object> data = extractData(response);
        parentCategoryId = ((Number) data.get("id")).longValue();
        assertThat(parentCategoryId).isNotNull();

        System.out.println("부모 카테고리 생성 ID: " + parentCategoryId);
    }

    @Test
    @Order(2)
    @DisplayName("2. 자식 카테고리 생성 (계층 구조)")
    void 자식_카테고리_생성() {
        // given
        SaveCategoryRequest request = new SaveCategoryRequest(
                "Java 게시판",
                CategoryType.BOARD_COMMON,
                parentCategoryId  // 부모 지정
        );

        // when
        ResponseEntity<Map> response = authenticatedPost(
                getBaseUrl() + "/categories",
                request,
                jwtToken,
                Map.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(isSuccess(response)).isTrue();

        Map<String, Object> data = extractData(response);
        childCategoryId = ((Number) data.get("id")).longValue();

        System.out.println("자식 카테고리 생성 ID: " + childCategoryId);
    }

    @Test
    @Order(3)
    @DisplayName("3. 전체 카테고리 목록 조회")
    void 카테고리_목록_조회() {
        // when
        ResponseEntity<Map> response = authenticatedGet(
                getBaseUrl() + "/categories",
                jwtToken,
                Map.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(isSuccess(response)).isTrue();

        List<Map<String, Object>> categories = extractData(response);
        assertThat(categories).hasSizeGreaterThanOrEqualTo(2);

        System.out.println("조회된 카테고리 수: " + categories.size());
    }

    @Test
    @Order(4)
    @DisplayName("4. 단건 카테고리 조회")
    void 카테고리_단건_조회() {
        // when
        ResponseEntity<Map> response = authenticatedGet(
                getBaseUrl() + "/categories/" + parentCategoryId,
                jwtToken,
                Map.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(isSuccess(response)).isTrue();

        Map<String, Object> data = extractData(response);
        assertThat(data.get("name")).isEqualTo("개발 게시판");

        // 자식 카테고리도 포함되어야 함
        List<Map<String, Object>> children = (List<Map<String, Object>>) data.get("children");
        assertThat(children).isNotEmpty();

        System.out.println("부모 카테고리 이름: " + data.get("name"));
        System.out.println("자식 카테고리 수: " + children.size());
    }

    @Test
    @Order(5)
    @DisplayName("5. 카테고리 수정")
    void 카테고리_수정() {
        // given
        SaveCategoryRequest request = new SaveCategoryRequest(
                "개발 게시판 (수정됨)",
                CategoryType.BOARD_COMMON,
                null
        );

        // when
        ResponseEntity<Map> response = authenticatedPut(
                getBaseUrl() + "/categories/" + parentCategoryId,
                request,
                jwtToken,
                Map.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 수정 확인
        ResponseEntity<Map> getResponse = authenticatedGet(
                getBaseUrl() + "/categories/" + parentCategoryId,
                jwtToken,
                Map.class
        );
        Map<String, Object> data = extractData(getResponse);
        assertThat(data.get("name")).isEqualTo("개발 게시판 (수정됨)");
    }

    @Test
    @Order(6)
    @DisplayName("6. 자식 카테고리 먼저 삭제")
    void 자식_카테고리_삭제() {
        // when
        ResponseEntity<Map> response = authenticatedDelete(
                getBaseUrl() + "/categories/" + childCategoryId,
                jwtToken,
                Map.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(categoryRepository.findById(childCategoryId)).isEmpty();

        System.out.println("자식 카테고리 삭제 완료: " + childCategoryId);
    }

    @Test
    @Order(7)
    @DisplayName("7. 부모 카테고리 삭제")
    void 부모_카테고리_삭제() {
        // when
        ResponseEntity<Map> response = authenticatedDelete(
                getBaseUrl() + "/categories/" + parentCategoryId,
                jwtToken,
                Map.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(categoryRepository.findById(parentCategoryId)).isEmpty();

        System.out.println("부모 카테고리 삭제 완료: " + parentCategoryId);
    }

    @Test
    @Order(8)
    @DisplayName("8. 존재하지 않는 카테고리 조회 시 에러")
    void 존재하지_않는_카테고리_조회() {
        // when
        ResponseEntity<Map> response = authenticatedGet(
                getBaseUrl() + "/categories/99999",
                jwtToken,
                Map.class
        );

        // then
        assertThat(response.getBody().get("status")).isEqualTo("ERROR");
    }
}
