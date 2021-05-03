package taplinkbot.telegram;

public enum BotContext {

    Canvas("canvas", "c"),
    LadyArt("ladyart", "l"),
    Ping("ping", "p");

    public final String name;
    public final String alias;

    BotContext(String name, String alias) {

        this.name = name;
        this.alias = alias;
    }

}
