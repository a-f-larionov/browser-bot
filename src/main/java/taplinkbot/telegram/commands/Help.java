package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.telegram.*;

import java.util.Arrays;
import java.util.Map;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/help")
public class Help extends Command {

    @Override
    public String getDescription() {
        return "Выведет список команд.";
    }

    /**
     * @param msg
     * @return
     * @todo рефакторинг
     */
    @Override
    public Message run(Request msg) {

        StringBuilder builder = new StringBuilder();

        Map<String, Command> commands = CommandExecutor.getCommands();

        Object[] keys = commands.keySet().toArray();

        Arrays.sort(keys);

        for (Object key : keys) {
            Command command = commands.get(key);

            builder.append(key.toString());
            builder.append(" - ");
            builder.append(command.getDescription());
            builder.append("\r\n");
        }

        return MessageBuilder.buildResult(builder.toString());
    }
}
