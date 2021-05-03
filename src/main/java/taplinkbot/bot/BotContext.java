package taplinkbot.bot;

public enum BotContext {

    Canvas("canvas", "c", Profile.Canvas),
    LadyArt("ladyart", "l", Profile.LadyArt);

    public final String name;
    public final String alias;
    public final Profile profile;

    BotContext(String name, String alias, Profile profile) {

        this.name = name;
        this.alias = alias;
        this.profile = profile;

    }
}
