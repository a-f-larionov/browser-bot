package taplinkbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init() {

        // Приложение работает по московскому времени.
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3"));
    }
}
