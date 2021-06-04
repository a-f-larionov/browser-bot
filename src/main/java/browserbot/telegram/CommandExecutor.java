package browserbot.telegram;

import browserbot.BrowserBotException;
import browserbot.bots.taplink.Profile;
import browserbot.services.ExceptionHandler;
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
public class CommandExecutor {

    private final CommandProccesor commandProccesor;

    private final RequestBuilder requestBuilder;

    private final Accessor accessor;

    private final TelegramBot telegramBot;

    private final ExceptionHandler exceptionHandler;

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

    /**
     * Выполняет команду из запроса команды.
     *
     * @param request
     */
    synchronized public void execute(Request request) {

        Reponse message;

        try {
            if (!accessor.check(request)) throw new BrowserBotException("Нет доступа.");

            Command command = commands.get(request.command);

            if (command == null) throw new BrowserBotException("Не удалось найти команду.");

            message = command.run(request);

        } catch (Exception e) {

            message = exceptionHandler.handle(e);
        }

        telegramBot.notify(request, message);
    }

    synchronized public void execute(Class<? extends Command> commandClass) {

        execute(commandClass, null);
    }

    synchronized public void execute(Class<? extends Command> commandClass, Profile profile) {

        Request request;

        request = requestBuilder.buildFromCommandClass(commandClass);

        request.profile = profile;
        //@todo strange
        request.skipCheckRights = true;

        execute(request);
    }
}