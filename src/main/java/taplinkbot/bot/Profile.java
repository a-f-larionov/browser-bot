package taplinkbot.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Список профилей на taplink.ru
 */
@Getter
@RequiredArgsConstructor
public enum Profile {

    Canvas("canvas.ru.com", "canvas.ru.com"),
    LadyArt("lady-art.art", "lady-art.art");

    /**
     * Текст в названии профиля в кабинете.
     * По нему бот будет определять профиль например для его смены.
     */
    private final String htmlText;

    /**
     * Доменное имя рекламной страницы.
     */
    private final String domainName;
}
