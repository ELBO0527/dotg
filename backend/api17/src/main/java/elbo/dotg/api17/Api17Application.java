package elbo.dotg.api17;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Api17Application {
	public static void main(String[] args) {
		SpringApplication.run(Api17Application.class, args);
	}
}