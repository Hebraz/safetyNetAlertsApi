package com.safetynet.alerts.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SafetyNetAlertsApiApplication  implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SafetyNetAlertsApiApplication.class, args);
	}

	@Autowired
	private AlertsDataSource dataSource;

	@Override
	public void run(String... args) throws Exception {
		dataSource.load();
		//test
		dataSource.getData()
				.getFirestations()
				.stream()
				.forEach(f -> System.out.println("Station" + f.getStation() + " at address " + f.getAddress()));
	}
}
