package ddbms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        Connector.get();
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
      return (args) -> {
          storageService.deleteAll();
          storageService.init();
        };
    }
}
