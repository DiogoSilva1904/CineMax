package deti.tqs.cinemax;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@AllArgsConstructor
public class CinemaxApplication implements CommandLineRunner {

	@Autowired
	private SetupData setupData;

	public static void main(String[] args) {
		SpringApplication.run(CinemaxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception
	{
		setupData.setup();
	}
}
