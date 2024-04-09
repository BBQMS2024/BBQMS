package ba.unsa.etf.si.bbqms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class BbqmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BbqmsApplication.class, args);
	}

}
