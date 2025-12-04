package elbo.dotg.api17;

import elbo.dotg.api17.domain.product.Product;
import elbo.dotg.api17.repository.product.ProductRepository;
import elbo.dotg.api17.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    private String couponKey;
    private Product product;

    /*@BeforeEach
    void beforeEach() {
        productRepository.saveAndFlush(new Product(1L, "T-shirts",1000, 900));
    }*/

    @Test
    void 동시에_100명이_주문해도_재고는_90개까지만_처리됨() throws InterruptedException {
        Long productId = productRepository.saveAndFlush(new Product(1L, "T-shirts",5000, 90)).getId();
        int numberOfThreads = 100;
        //long productId = 4002L;

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    productService.orderWithLock(productId);
                    successCount.incrementAndGet();//주문 성공 카운트 +1
                } catch (Exception e) {
                    failCount.incrementAndGet();//재고없음 카운트 +1
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Product result = productRepository.findById(productId).orElseThrow();

        Assertions.assertEquals(0, result.getQuantity());
        Assertions.assertEquals(90, successCount.get());
        Assertions.assertEquals(10, failCount.get());

        System.out.println("남은 재고: " + result.getQuantity());
        System.out.println("성공 수: " + successCount.get());
        System.out.println("실패 수: " + failCount.get());
    }

    @Test
    void 락없이_동시에_100건_주문() throws InterruptedException {
        Long productId = productRepository.saveAndFlush(new Product(1L, "T-shirts",5000, 80)).getId();

        int numberOfThreads = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    productService.orderWithoutLock(productId);
                    successCount.incrementAndGet();//주문 성공 카운트 +1
                } catch (Exception e) {
                    failCount.incrementAndGet();//재고없음 카운트 +1
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Product result = productRepository.findById(productId).orElseThrow();

        System.out.println("남은 재고: " + result.getQuantity());
        System.out.println("성공 수: " + successCount.get());
        System.out.println("실패 수: " + failCount.get());
    }

}
