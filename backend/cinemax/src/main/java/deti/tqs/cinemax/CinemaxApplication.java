package deti.tqs.cinemax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CinemaxApplication implements org.springframework.boot.CommandLineRunner
{

	public static void main(String[] args) {
		SpringApplication.run(CinemaxApplication.class, args);
	}
	
	@Autowired
	private SetupData setupData;
	
	@Override
	public void run(String... args) throws Exception
	{
		setupData.setup();
	}
	


}
