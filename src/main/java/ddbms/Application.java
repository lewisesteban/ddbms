package ddbms;

import ddbms.models.Article;
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
            Connector.get().createArticle(new Article("1234567", System.currentTimeMillis(), "title", "category", "abs", "tag", "genius", "english", "bla bla bla", "none", "no video"));
            Connector.get().getArticle("0");
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
