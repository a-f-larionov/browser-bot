package taplinkbot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


/**
 * Приложение TapLinkBot.
 * Выполняет автоматизацию процессов в кабинетах taplink.com
 *
 * @link https://taplink.ru
 */
@SpringBootApplication
@EnableScheduling
@Slf4j
public class Application {

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);

        setContext(run);
    }

    @PostConstruct
    private void init() {

        /* Приложение работает по московскому времени. */
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3"));
    }
}
