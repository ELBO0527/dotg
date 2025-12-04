package elbo.dotg.api17;

import elbo.dotg.api17.domain.product.Product;
import elbo.dotg.api17.repository.product.KafkaRedisIncrRepository;
import elbo.dotg.api17.repository.product.ProductRepository;
import elbo.dotg.api17.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@EnableKafka
//@EmbeddedKafka(partitions = 1,
//        brokerProperties = {"listeners=PLAINTEXT://localhost:9092"},
//        ports = { 9092 }
//)
@Slf4j
@SpringBootTest
class KafkaTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private KafkaRedisIncrRepository redisIncrRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafka;

//    @Test
//    void testSend() throws Exception {
//        kafka.send("order-topic", "1:1").get();
//        Thread.sleep(3000);
//    }

//    @KafkaListener(topics = "order-topic", groupId = "test-group")
//    public void listen(String msg) {
//        System.out.println("Consumed message: " + msg);
//    }

    @BeforeEach
    void beforeEach() {
        productRepository.saveAndFlush(new Product(1L, "T-shirts",1000, 50000));
    }

    @AfterEach
    void afterEach() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("[Kafka + Redis Lock]")
    void test() throws Exception {
        final AtomicInteger successCounter = new AtomicInteger(0);
        final AtomicInteger failCounter = new AtomicInteger(0);
        int threadCount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        try {
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        productService.decrese(1L, 1L);
                        successCounter.incrementAndGet();
                    } catch (IllegalArgumentException e) {
                        log.error("Stock Exception. {}", e.getMessage());
                        failCounter.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(); // 모든 스레드 완료까지 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executorService.shutdown();
        }
        sleep(10000);

        final Long count = redisIncrRepository.getCount(1L);
        log.info("Redis Final Count: {}", count);  // Redis에서 최종 카운트 확인

        log.info("Success Count: {}", successCounter.get());
        log.info("Fail Count: {}", failCounter.get());
        assertEquals(2000, count);
        //assertEquals(1000, successCounter.get());
        //assertEquals(1000, failCounter.get());
    }
}