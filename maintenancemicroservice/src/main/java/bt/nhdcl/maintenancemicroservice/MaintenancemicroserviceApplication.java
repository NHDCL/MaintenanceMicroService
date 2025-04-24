package bt.nhdcl.maintenancemicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MaintenancemicroserviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MaintenancemicroserviceApplication.class, args);
	}
}
