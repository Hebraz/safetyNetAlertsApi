package com.safetynet.alerts.api;

import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import com.safetynet.alerts.api.model.Person;
import com.safetynet.alerts.api.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class SafetyNetAlertsApiApplication  implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SafetyNetAlertsApiApplication.class, args);
	}

	@Autowired
	private IAlertsDataSource dataSource;

	@Autowired
	private PersonService personService;

	@Value( "${datasource.filepath}" )
	private String dataSourceFilePath;

	@Override
	public void run(String... args) throws Exception {

		// Creating an object of TimeZone class.
		TimeZone time_zone_default
				= TimeZone.getDefault();

		// Displaying the default TimeZone
		System.out.println("Default TimeZone: "
				+ time_zone_default);


		dataSource.load(dataSourceFilePath);
		//test
		dataSource.getData()
				.getFirestations()
				.stream()
				.forEach(f -> System.out.println("Station" + f.getStation() + " at address " + f.getAddress()));

		personService.deletePerson("Brian", "Stelzer");

		Person p = new Person();
		p.setFirstName("Pierrick");
		p.setLastName("LEVAZEUX");
		p.setAddress("145 Rue Pierre Brossolette");
		p.setCity("Noisy le grand");
		p.setZip("93160");
		p.setPhone("06 13 72 61 16");
		p.setEmail("pierrick.levazeux@hotmail.com");

		personService.savePerson(p);

		dataSource.getData()
				.getPersons()
				.stream()
				.forEach(f -> System.out.println(f.getFirstName() + " " + f.getLastName() + " : "  + f.getAddress() +
				" , " + f.getCity() + " (" + f.getZip() + ") Phone : " + f.getPhone() + ". Email : " + f.getEmail() ));

		Person p1 = new Person();
		p1.setFirstName("Eric");
		p1.setLastName("Cadigan");
		p1.setAddress("146 Rue Pierre Brossolette");
		p1.setCity("Noisy-le-grand");
		p1.setZip("93161");
		p1.setPhone("06 13 72 61 17");
		p1.setEmail("Eric.Cadigan@hotmail.com");

		personService.savePerson(p1);

		dataSource.getData()
				.getPersons()
				.stream()
				.forEach(f -> System.out.println(f.getFirstName() + " " + f.getLastName() + " : "  + f.getAddress() +
						" , " + f.getCity() + " (" + f.getZip() + ") Phone : " + f.getPhone() + ". Email : " + f.getEmail() ));

	}
}
