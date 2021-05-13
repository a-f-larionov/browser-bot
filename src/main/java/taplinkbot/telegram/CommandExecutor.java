package taplinkbot.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import taplinkbot.TapLinkBotException;

import javax.annotation.PostConstruct;
import java.util.Hashtable;
import java.util.Map;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
@Slf4j
public class CommandExecutor {

    private final CommandProccesor commandProccesor;

    private final Accessor accessor;

    private final TelegramBot telegramBot;

    /**
     * Hashtable no have null and synchronized
     */
    @Getter
    private static final Map<String, Command> commands = new Hashtable<>();

    @PostConstruct
    private void init() {
        commandProccesor.setCommandExecutor(this);
    }

    public static void addCommand(String name, Command commandObject) {

        if (commands.containsKey(name)) {
            throw new RuntimeException("Команда " + name + "уже существует.");
        }

        commands.put(name, commandObject);
    }

    public void execute(Request request) {

        Message message;

        try {
            if (!accessor.check(request)) throw new TapLinkBotException("Нет доступа.");

            Command command = commands.get(request.command);

            if (command == null) throw new TapLinkBotException("Не удалось найти команду.");

            message = command.run(request);

        } catch (Exception e) {

            message = MessageBuilder.buildFailed("Не удалось выполнить команду.", e);
        }

        telegramBot.notify(request, message);
    }
}