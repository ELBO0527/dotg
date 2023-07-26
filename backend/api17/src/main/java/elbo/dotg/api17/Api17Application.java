package elbo.dotg.api17;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@PropertySource("classpath:/dev-secure.properties")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Api17Application {
	public static void main(String[] args) {
		SpringApplication.run(Api17Application.class, args);
	}
}