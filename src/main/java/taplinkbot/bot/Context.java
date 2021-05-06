package taplinkbot.bot;

import lombok.Getter;
import org.springframework.core.env.ConfigurableEnvironment;
import taplinkbot.Application;

@Getter
public enum Context {

    Canvas("canvas", "c", Profile.Canvas, "canvas.ru.com", "canvas.ru.com"),
    LadyArt("ladyart", "l", Profile.LadyArt, "lady-art.art", "lady-art.art");

    public final String name;
    public final String alias;
    public final Profile profile;

    /**
     * Текст в названии профиля в кабинете.
     * По нему бот будет определять профиль например для его смены.
     */
    private final String htmlText;

    /**
     * Доменное имя рекламной страницы.
     */
    private final String domainName;

    Context(String name, String alias, Profile profile, String htmlText, String domainName) {

        this.name = name;
        this.alias = alias;
        this.profile = profile;

        this.htmlText = htmlText;
        this.domainName = domainName;
    }

    public String getLogin() {
        return getProp("username");
    }

    public String getPassword() {
        return getProp("password");
    }

    private String getProp(String name) {

        ConfigurableEnvironment env = Application.getContext().getEnvironment();

        String envPropName = "taplink." + this.name + "." + name;

        return env.getProperty(envPropName);
    }
}
