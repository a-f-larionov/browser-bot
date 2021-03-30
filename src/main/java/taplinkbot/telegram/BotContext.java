package taplinkbot.telegram;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum BotContext {

    CanvasRuCom("canvas", "c"),
    Cabinet2("cabinet2"),
    Ping("ping", "p");

    public final String[] stringNames;

    BotContext(String... names) {

        this.stringNames = names;
    }

    public static BotContext getByString(String arg) {

        for (BotContext botContext : BotContext.values()) {

            if (Arrays.stream(botContext.stringNames).anyMatch(arg::equals)) {
                return botContext;
            }
        }
        return null;
    }

    public static String getValuesCommaString() {
        return Arrays.asList(BotContext.values())
                .stream()
                .map((BotContext botContext) -> {
                    return botContext.stringNames[0];
                })
                .collect(Collectors.joining(","));

    }
}
