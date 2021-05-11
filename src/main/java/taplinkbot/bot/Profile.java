package taplinkbot.bot;

import lombok.Getter;

@Getter
public enum Profile {

    Canvas("canvas", "c", "canvas.ru.com", "canvas.ru.com"),
    LadyArt("ladyart", "l", "lady-art.art", "lady-art.art");

    public final String name;
    public final String alias;

    /**
     * Текст в названии профиля в кабинете.
     * По нему бот будет определять профиль например для его смены.
     */
    private final String htmlText;

    /**
     * Доменное имя рекламной страницы.
     */
    private final String domainName;

    Profile(String name, String alias, String htmlText, String domainName) {

        this.name = name;
        this.alias = alias;

        this.htmlText = htmlText;
        this.domainName = domainName;
    }

    /**
     * Возвращает ссылку на страницу Мультиссылки.
     *
     * @return мультиссылка
     * @todo move to Profile
     */
    public String getPageUrl() {
        return "https://" + this.getDomainName() + "/";
    }
}
