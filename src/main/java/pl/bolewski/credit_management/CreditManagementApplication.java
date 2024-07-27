package pl.bolewski.credit_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CreditManagementApplication {

//	private static final Logger log = LoggerFactory.getLogger(CreditManagementApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CreditManagementApplication.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(MoneyRepository repo) {
//		return args -> {
//			if(repo.count() ==0) {
//				repo.save(Money.builder().id(1).cash(1000L).date(LocalDate.now()).build());
//				log.info("Money saved");
//			}
//		};
//	}
}
