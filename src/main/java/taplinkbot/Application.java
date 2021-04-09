package taplinkbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class Application {

    private static Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public Application(Environment env) {
        log.info(env.getProperty("spring.config.activate.on-profile"));
        log.info(env.getProperty("application.mode"));
    }

    @PostConstruct
    public void init() {

        /** Приложение работает по московскому времени. */
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3"));
    }
}
