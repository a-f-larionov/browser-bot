package taplinkbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BotContexts {

    private Context botContext;

    public void setCurrent(Context botContext) {

        if (botContext != null && this.botContext != null) {
            log.info("bot context is busy by:" + this.botContext.name
                    + "!!! Requested: " + botContext.name);
        }

        this.botContext = botContext;
    }

    public Context current() {
        return botContext;
    }

    public static Context getByString(String arg) {

        for (Context botContext : Context.values()) {
            if (botContext.name.equals(arg) ||
                    botContext.alias.equals(arg)) {
                return botContext;
            }
        }

        return null;
    }

    public static String getValuesCommaString() {

        return Arrays.asList(taplinkbot.telegram.BotContext.values())
                .stream()
                .map((taplinkbot.telegram.BotContext botContext) -> {
                    return botContext.name;
                })
                .collect(Collectors.joining(","));
    }
}
