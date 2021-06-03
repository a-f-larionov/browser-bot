//FIN
package browserbot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
@RequiredArgsConstructor
public class Application {

    @Value("${app.timezone}")
    private String timeZone;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    private void init() {
        /* Приложение работает по московскому времени. */
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }
}