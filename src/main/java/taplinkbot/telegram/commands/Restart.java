//FIN
package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@TelegramCommand(name = "/restart")
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
    public Response run(Message msg) {
        SpringApplication.exit(context);

        Runtime.getRuntime().exit(0);

        return ResponseFactory.buildSuccessReponse();
    }
}