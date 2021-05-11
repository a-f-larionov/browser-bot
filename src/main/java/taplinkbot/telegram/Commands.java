package taplinkbot.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
@Slf4j
public class Commands {

    private final Router router;

    @Getter
    private static final Map<String, TelegramCommandInterface> commands = new HashMap<>();

    @PostConstruct
    private void init() {
        router.setCommands(this);
    }

    public static void addCommand(String name, TelegramCommandInterface commandObject) {

        commands.put(name, commandObject);
    }

    public Response execute(Message request) {

        TelegramCommandInterface command = commands.get(request.cammand);

        if (command == null) {
            return new Response("Команда не найдена");
        }

        return command.run(request);
    }
}