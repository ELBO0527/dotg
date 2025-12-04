package elbo.dotg.api17.repository.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KafkaRedisIncrRepository {
    private final RedisTemplate<String,String> redisTemplate;

    public Long increment(final Long productId){
        return redisTemplate.opsForValue().increment(Long.toString(productId));
    }

    public Long getCount(final Long id){
        final String result = redisTemplate.opsForValue().get(Long.toString(id));
        return Long.parseLong(result == null ? "0" : result);
    }
}
