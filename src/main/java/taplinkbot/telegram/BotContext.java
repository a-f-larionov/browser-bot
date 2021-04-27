package taplinkbot.telegram;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum BotContext {

    Canvas("Canvas", "c"),
    LadyArt("ladyart", "l"),
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
