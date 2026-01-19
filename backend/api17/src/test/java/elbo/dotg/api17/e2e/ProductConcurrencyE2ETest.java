package elbo.dotg.api17.e2e;

import elbo.dotg.api17.domain.product.Product;
import elbo.dotg.api17.repository.product.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 동시성 E2E 테스트
 * - 실제 HTTP 요청으로 동시성 문제 검증
 * - 분산락(Redisson) 동작 확인
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class ProductConcurrencyE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1";
        productRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("동시에 100명이 주문해도 재고가 정확히 차감됨 (E2E)")
    void 동시_주문_재고_정합성_테스트() throws InterruptedException {
        // given - 재고 50개 상품 생성
        int initialStock = 50;
        Product product = productRepository.saveAndFlush(
                Product.builder()
                        .name("E2E 테스트 상품")
                        .price(10000)
                        .quantity(initialStock)
                        .build()
        );
        Long productId = product.getId();

        int numberOfThreads = 100;  // 100명 동시 주문
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch startLatch = new CountDownLatch(1);  // 동시 시작용
        CountDownLatch endLatch = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        List<Future<?>> futures = new ArrayList<>();

        // when - 100개 요청 동시 실행
        for (int i = 0; i < numberOfThreads; i++) {
            futures.add(executorService.submit(() -> {
                try {
                    startLatch.await();  // 모든 스레드가 준비될 때까지 대기

                    ResponseEntity<Map> response = restTemplate.postForEntity(
                            baseUrl + "/products/" + productId + "/order",
                            null,
                            Map.class
                    );

                    if (response.getStatusCode().is2xxSuccessful()) {
                        successCount.incrementAndGet();
                    } else {
                        failCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    endLatch.countDown();
                }
            }));
        }

        // 모든 스레드 동시 시작
        startLatch.countDown();
        endLatch.await(30, TimeUnit.SECONDS);
        executorService.shutdown();

        // then
        Product result = productRepository.findById(productId).orElseThrow();

        System.out.println("========== 동시성 테스트 결과 ==========");
        System.out.println("초기 재고: " + initialStock);
        System.out.println("동시 요청 수: " + numberOfThreads);
        System.out.println("성공한 주문: " + successCount.get());
        System.out.println("실패한 주문: " + failCount.get());
        System.out.println("남은 재고: " + result.getQuantity());
        System.out.println("======================================");

        // 검증 - 재고는 0이어야 하고, 성공 수는 초기 재고와 같아야 함
        assertThat(result.getQuantity()).isEqualTo(0);
        assertThat(successCount.get()).isEqualTo(initialStock);
        assertThat(failCount.get()).isEqualTo(numberOfThreads - initialStock);
    }

    @Test
    @DisplayName("상품 목록 조회는 동시 요청에도 안정적으로 동작")
    void 동시_조회_안정성_테스트() throws InterruptedException {
        // given - 테스트용 상품 생성
        for (int i = 0; i < 10; i++) {
            productRepository.save(
                    Product.builder()
                            .name("상품 " + i)
                            .price(1000 * i)
                            .quantity(100)
                            .build()
            );
        }

        int numberOfThreads = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // when
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    ResponseEntity<Map> response = restTemplate.getForEntity(
                            baseUrl + "/products",
                            Map.class
                    );

                    if (response.getStatusCode().is2xxSuccessful()) {
                        successCount.incrementAndGet();
                    } else {
                        failCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();

        // then
        System.out.println("동시 조회 성공: " + successCount.get());
        System.out.println("동시 조회 실패: " + failCount.get());

        assertThat(successCount.get()).isEqualTo(numberOfThreads);
        assertThat(failCount.get()).isEqualTo(0);
    }
}
