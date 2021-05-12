package taplinkbot.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Hashtable;
import java.util.Map;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
@Slf4j
public class Commands {

    private final Router router;

    /**
     * Hashtable no have null and synchronized
     */
    @Getter
    private static final Map<String, CommandInterface> commands = new Hashtable<>();

    @PostConstruct
    private void init() {
        router.setCommands(this);
    }

    public static void addCommand(String name, CommandInterface commandObject) {

        if (commands.containsKey(name)) {
            //@todo
        }
        commands.put(name, commandObject);
    }

    public Response execute(Message request) {

        CommandInterface command = commands.get(request.cammand);

        if (command == null) {
            return new Response("Команда не найдена");
        }

        return command.run(request);
    }
}