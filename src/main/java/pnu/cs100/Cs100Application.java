package pnu.cs100;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Cs100Application {

	public static void main(String[] args) {
		SpringApplication.run(Cs100Application.class, args);
	}

}
