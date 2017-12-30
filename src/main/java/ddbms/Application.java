package ddbms;

import ddbms.models.Article;
import ddbms.models.BeRead;
import ddbms.models.Read;
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
        SpringApplication.run(Application.class, args);

        try {
            Dal.get().createBeread(new BeRead("a"));
            BeRead r = Dal.get().getBeRead("a");
            r.agree("u", false);
            Dal.get().updateBeread(r);
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
