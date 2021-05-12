//FIN
package taplinkbot.bot;

import lombok.Getter;

@Getter
public enum Profile {

    Canvas("canvas", "c", "canvas.ru.com", "canvas.ru.com"),
    LadyArt("ladyart", "l", "lady-art.art", "lady-art.art"),

    Test1("larionov", "a", "a.f.larionov", "taplink.cc/a.f.larionov"),
    Test2("id5006243", "i", "id:5006243", "taplink.cc/id:5006243");

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
    public String getPageUrl() {
        return "https://" + this.getDomainName();
    }
}
