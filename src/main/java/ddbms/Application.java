package ddbms;

import ddbms.storage.StorageProperties;
import ddbms.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.sql.SQLException;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Application {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Program argument required: 'beijing' or 'hongkong'");
            System.exit(1);
        }

        SpringApplication.run(Application.class, args);

        try {
            String region = null;
            switch (args[0]) {
                case "beijing":
                    region = "Beijing";
                    break;
                case "hongkong":
                    region = "Hong Kong";
                    break;
                default:
                    System.err.println("Program argument required: 'beijing' or 'hongkong'");
                    System.exit(1);
            }
            Domain.setRegion(region);
            Dal.get().initDb(region);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
      return (args) -> {
          storageService.deleteAll();
          storageService.init();
        };
    }
}
