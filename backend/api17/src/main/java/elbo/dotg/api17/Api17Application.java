package elbo.dotg.api17;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Api17Application {
	public static void main(String[] args) {
		SpringApplication.run(Api17Application.class, args);
	}
}