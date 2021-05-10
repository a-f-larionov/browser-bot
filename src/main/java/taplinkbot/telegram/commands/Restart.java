package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import taplinkbot.telegram.Command;
import taplinkbot.telegram.CommandInterface;
import taplinkbot.telegram.Message;
import taplinkbot.telegram.Response;

@Component
@Command(name = "/restart")
@RequiredArgsConstructor
public class Restart implements CommandInterface {

    private final ConfigurableApplicationContext context;

    @Override
    public Response run(Message msg) {
        SpringApplication.exit(context);

        Runtime.getRuntime().exit(0);

        return new Response("Перезапущено");
    }
}
