package elbo.dotg.api17.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
//@PropertySource("classpath:/dev-docker-prod.properties") //배포
@PropertySource("classpath:/dev-secure.properties") //로컬
@Configuration
public class SpringConfig {}
