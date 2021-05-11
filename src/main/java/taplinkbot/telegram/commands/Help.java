package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.telegram.*;

import java.util.Arrays;
import java.util.Map;

@Component
@RequiredArgsConstructor
@TelegramCommand(name = "/help")
public class Help implements TelegramCommandInterface {

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
    public Response run(Message msg) {

        StringBuilder builder = new StringBuilder();

        Map<String, TelegramCommandInterface> commands = Commands.getCommands();

        Object keys[] = commands.keySet().toArray();

        Arrays.sort(keys);

        for (Object key : keys) {
            TelegramCommandInterface command = commands.get(key);

            builder.append(key.toString());
            builder.append(" - ");
            builder.append(command.getDescription());
            builder.append("\r\n");
        }

        return ResponseFactory.buildSuccessReponse(builder.toString());
    }
}
