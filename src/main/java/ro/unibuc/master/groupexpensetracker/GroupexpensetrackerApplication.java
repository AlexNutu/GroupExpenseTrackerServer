package ro.unibuc.master.groupexpensetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:application.properties", "classpath:group-expensive-tracker.properties"})
public class GroupexpensetrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroupexpensetrackerApplication.class, args);
	}

}
