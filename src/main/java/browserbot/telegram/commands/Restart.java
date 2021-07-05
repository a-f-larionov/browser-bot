//FIN
package browserbot.telegram.commands;

import browserbot.telegram.annotations.CommandClass;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import browserbot.telegram.*;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/restart")
public class Restart extends Command {

    private final ConfigurableApplicationContext context;

    @Override
    public String getDescription() {
        return "Перезагрузит сервер";
    }

    /**
     * Завершит работу спринг приложения.
     * Перезагрузку соврешит shell скрипт. см. init.d/taplinkb
     */
    @Override
    public Reponse run(Request msg) {
        SpringApplication.exit(context);

        Runtime.getRuntime().exit(0);

        return MessageBuilder.buildResult();
    }
}