package manage.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication()
public class StoreManagerApplication {
	public static void main(String[] args) {
		init();

		SpringApplication.run(StoreManagerApplication.class, args);
	}

	private static void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
}
