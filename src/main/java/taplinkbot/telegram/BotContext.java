package taplinkbot.telegram;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum BotContext {

    CanvasRuCom("canvas", "c"),
    Cabinet2("cabinet2", "cabinet2"),
    Ping("ping", "p");

    public final String name;
    public final String alias;

    BotContext(String name, String alias) {

        this.name = name;
        this.alias = alias;
    }

    public static BotContext getByString(String arg) {

        for (BotContext botContext : BotContext.values()) {
            if (botContext.name.equals(arg) ||
                    botContext.alias.equals(arg)) {
                return botContext;
            }
        }

        return null;
    }

    public static String getValuesCommaString() {

        return Arrays.asList(BotContext.values())
                .stream()
                .map((BotContext botContext) -> {
                    return botContext.name;
                })
                .collect(Collectors.joining(","));
    }
}
