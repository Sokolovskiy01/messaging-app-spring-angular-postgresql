package sokol.messagingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MessagingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessagingAppApplication.class, args);
	}

}
