package elbo.dotg.api17.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @org.springframework.kafka.annotation.KafkaListener(topics = "1-topic", groupId = "topic-group-01")
    public void sendTest(ConsumerRecord<String, Object> record){
        logger.info("send message : {}", record.value());
        logger.info("send key : {}", record.key());
        logger.info("record debug : {}", record);
    }
}
