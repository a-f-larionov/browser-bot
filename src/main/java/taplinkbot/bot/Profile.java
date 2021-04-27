package taplinkbot.bot;

import lombok.Getter;

/**
 * Список профилей на taplink.ru
 */
@Getter
public enum Profile {

    Canvas("canvas.ru.com", "canvas.ru.com"),
    LadyArt("lady-art.art", "lady-art.art");

    /**
     * Текст в названии профиля к кабинете.
     * По нему бот будет определять профиль например для его смены.
     */
    private final String htmlText;

    /**
     * Доменное имя рекламной страницы.
     */
    private final String domainName;

    Profile(String htmlText, String domainName) {
        this.htmlText = htmlText;
        this.domainName = domainName;
    }
}
