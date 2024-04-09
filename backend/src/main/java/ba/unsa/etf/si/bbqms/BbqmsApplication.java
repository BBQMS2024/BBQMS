package ba.unsa.etf.si.bbqms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BbqmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BbqmsApplication.class, args);
	}

}
