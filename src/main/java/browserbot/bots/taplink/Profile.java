//FIN
package browserbot.bots.taplink;

import lombok.Getter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Profile {

    Canvas("canvas", "c", "canvas.ru.com", "canvas.ru.com"),
    LadyArt("ladyart", "l", "lady-art.art", "lady-art.art");

    /**
     * Имя используется для указаний клиентом профиля по имени
     */
    public final String name;

    /**
     * Алиас используется для указаний клиентом профиля по алиасу
     */
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
     */
    public String getMultiPageUrl() {
        return "https://" + this.getDomainName();
    }

    /**
     * Найти соответствующий профиль
     *
     * @param needle текст для поиска
     * @return Profile соотвественный тексту или null
     */
    public static Profile searchByName(String needle) {

        return Stream.of(Profile.values())
                .filter(row -> row.searchMatches(needle))
                .findFirst()
                .orElse(null);
    }

    /**
     * Соответсвует ли текстовому поиску этот профиль.
     *
     * @param needle текст для поиска
     * @return true - если соответствует, false - иначе
     */
    private boolean searchMatches(String needle) {

        return name.equals(needle) || alias.equals(needle);
    }

    /**
     * Вызвращает список профиелей строкой.
     */
    public static String getListOfAll() {

        return Stream.of(Profile.values())
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }
}
