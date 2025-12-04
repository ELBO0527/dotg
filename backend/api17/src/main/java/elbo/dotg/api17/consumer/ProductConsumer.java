package elbo.dotg.api17.consumer;

import elbo.dotg.api17.repository.product.KafkaRedisIncrRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductConsumer {
    private final KafkaTemplate<String, Object> kafka;
    private final KafkaRedisIncrRepository redisIncrRepository;

    public void create(final Long userId, final Long productId){
        ListenableFuture<SendResult<String, Object>> future = (ListenableFuture<SendResult<String, Object>>) kafka.send("order-topic", productId);
                future.addCallback(
                        result -> log.info("✅ [Test] Kafka message sent: {}", productId),
                        ex -> log.error("❌ [Test] Kafka message failed: {}", productId, ex)
                );
    }

    //@KafkaListener(topics = "redis-kafka-concur", groupId = "topic-group-01")
    public void consume(String message) {
        log.info("🔥 Received message: {}", message);
        Long productId = Long.parseLong(message);
         redisIncrRepository.increment(productId);
    }
}
