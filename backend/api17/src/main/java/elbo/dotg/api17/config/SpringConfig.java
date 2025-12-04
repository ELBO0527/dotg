package elbo.dotg.api17.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;

@Slf4j
@Configuration
public class SpringConfig {}

@Profile("dev")
@Configuration
@PropertySource("classpath:/dev-secure.properties")
class LocalConfig {}

@Profile("prod")
@Configuration
@PropertySource("classpath:/dev-docker-prod.properties")
class ProdConfig {}