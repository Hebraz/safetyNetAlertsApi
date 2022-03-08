package com.safetynet.alerts.api;

import com.safetynet.alerts.api.datasource.IAlertsDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyNetAlertsApiApplication  implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SafetyNetAlertsApiApplication.class, args);
	}

	private final IAlertsDataSource dataSource;

	@Value( "${datasource.filepath}" )
	private String dataSourceFilePath;

	@Override
	public void run(String... args) throws Exception {
		dataSource.load(dataSourceFilePath);
	}
}
