package elbo.dotg.api17.service.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import elbo.dotg.api17.consumer.ProductConsumer;
import elbo.dotg.api17.domain.product.Product;
import elbo.dotg.api17.dto.request.order.OrderRequest;
import elbo.dotg.api17.dto.request.product.ProductRequest;
import elbo.dotg.api17.dto.response.product.ProductResponse;
import elbo.dotg.api17.repository.category.CategoryRepository;
import elbo.dotg.api17.repository.product.KafkaRedisIncrRepository;
import elbo.dotg.api17.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final RedissonClient redissonClient;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final KafkaRedisIncrRepository kafkaRedisIncrRepository;
    private final ProductConsumer productConsumer;
    public static long exQuantity = 0;

    private final int EMPTY = 0;

    //@KafkaListener(topics = {"order-topic","order-topic-autoExist"}, groupId = "topic-group-01")
    public void Order(@Payload String message) throws JsonProcessingException {
        //ObjectMapper 객체 생성
        ObjectMapper mapper = new ObjectMapper();

        //String에서 Json형식으로 불필요한 문자 제거
        String cleanMessage = message.replaceAll("^\"|\"$", ""); // 앞뒤 쌍따옴표 제거
        cleanMessage = cleanMessage.replace("\\\"", "\"");       // 이스케이프된 따옴표 제거

        //DTO로 전달받아 활용하는 경우
        OrderRequest request = mapper.readValue(cleanMessage, OrderRequest.class);

        //Producer의 직렬화 설정을를 String으로 하였을 때, Json 메세지를 String으로 넘긴 후
        //JsonNode 객체 활용하여 String => Json 컨버팅 가능
        JsonNode node = mapper.readTree(message);
        String productName = node.get("product").get("name").asText();


        log.info("받은 메시지 전체: {}", message);
        log.info("상품 Json : {}", mapper.writeValueAsString(request.getProduct()));
        log.info("JsonNode 상품 이름: {}", productName);
        log.info("상품 이름: {}", request.getProduct().getName());
        log.info("상품 ID 목록: {}", request.getProductIds());
        log.info("수량: {}", request.getQuantity());

        //추가 로직 구현...
    }

    public void decrese(final Long userId, final Long productId){
        final Product product = productRepository.findById(productId).orElseThrow();
        //레디스를 통해 임계값 확인
        final Long count = kafkaRedisIncrRepository.increment(productId);
        product.validQuantity(count);

        //실제 처리는 컨슈머가 처리
        productConsumer.create(userId, productId);
    }

    public void orderWithLock(final Long productId){
        final String lockName = "lock:product:" + productId.toString();
        final RLock lock = redissonClient.getLock(lockName);
        final String threadName = Thread.currentThread().getName();
        try {
            if (!lock.tryLock(2, 5, TimeUnit.SECONDS)) {
                throw new RuntimeException("락 획득 실패");
            }
            log.info("-------------<락 획득 성공>-------------");

            Product product = productRepository.findById(productId).orElseThrow();
            log.info("[수량 체크] Thread: {}, 현재 재고: {}", threadName, product.getQuantity());

            if (product.getQuantity() <= EMPTY) {
                log.warn("재고 없음");
                throw new IllegalStateException();
            }
            product.decrease();//재고 1개 차감
            productRepository.saveAndFlush(product);//db 저장
            Thread.sleep(50);//DB처리 시간 위한 지연시간
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("인터럽트 발생", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                log.info("---------------<락 해제>---------------");
                lock.unlock();
            }
        }
    }

    public void orderWithoutLock(final Long productId){
        final String threadName = Thread.currentThread().getName();
        Product product = productRepository.findById(productId).orElseThrow();
        log.info("[수량 체크] Thread: {}, 현재 재고: {}", threadName, product.getQuantity());

        if (product.getQuantity() <= EMPTY) {
            log.warn("재고 없음");
            throw new IllegalStateException();
        }
        product.decrease();//재고 1개 차감
        productRepository.saveAndFlush(product);//db 저장
        //Thread.sleep(50);//DB처리 시간 위한 지연시간
    }


    public void setAvailableProduct(String key, long quantity) {
        redissonClient.getBucket(key).set(quantity);
    }

    public int usableProduct(String key) {
        return (int) redissonClient.getBucket(key).get();
    }

    public List<ProductResponse> findAllPRoducts(){
            List<Product> products = productRepository.findAll();
            return products.stream().map(ProductResponse::from).collect(Collectors.toList());
    }

    public ProductResponse findProductById(final long id) throws Exception {
        Product product = productRepository.findById(id).orElseThrow(Exception::new);
        return ProductResponse.from(product);
    }

    /*public BoardResponse findBoardById(final long id){
        Product product = productRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        return BoardResponse.from(product);
    }*/

    /*public BoardResponse updateProductById(final long id, final UpdateBoardRequest boardUpdateRequest){
        findBoardById(id).toEntity().updateBoard(
                Board.builder()
                        .title(boardUpdateRequest.title())
                        .category(categoryRepository.findById(boardUpdateRequest.categoryId()).orElseThrow(CategoryNotFoundException::new))
                        .attachments(boardUpdateRequest.attachments())
                        .content(boardUpdateRequest.Content())
                .build());
        return BoardResponse.from(boardRepository.findById(id).orElseThrow(BoardNotFoundException::new));
    }*/

    public ProductResponse saveProduct(final ProductRequest productRequest){
        return ProductResponse.from(productRepository.save(Product.builder()
                        .name(productRequest.name())
                        .price(productRequest.price())
                        .quantity(productRequest.quantity())
                        .build()));
    }

    /*public long deleteBoardById(final long id){
        boardRepository.deleteById(id);
        return id;
    }*/
}
