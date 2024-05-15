package deti.tqs.cinemax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CinemaxApplication
		//implements CommandLineRunner
{



	public static void main(String[] args) {
		SpringApplication.run(CinemaxApplication.class, args);
	}


	/*

	Execute only one time, then comment back
	@Autowired
	private SetupData setupData;
	@Override
	public void run(String... args) throws Exception
	{
		setupData.setup();
	}

	 */


}
