package taplinkbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {

        /*
         * Приложение работает по московскому времени.
         * @Todo move to application.properties
         */
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3"));

        SpringApplication.run(Application.class, args);
    }
}
