package taplinkbot.telegram;

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

    @PostConstruct
    private void init() {
        router.setCommands(this);
    }

    private static Map<String, CommandInterface> commands = new HashMap<>();

    public static void addCommand(String name, CommandInterface commandObject) {

        commands.put(name, commandObject);
    }

    public Response execute(Message msg) throws Exception {

        CommandInterface command = commands.get(msg.cammand);

        if (command == null) {
            return new Response("Команда не найдена");
        }

        return command.run(msg);
    }
}