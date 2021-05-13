package taplinkbot.telegram;

import org.springframework.stereotype.Component;

@Component
//@todo some pattern may be? or spring accesor?
public class Accessor {

    /**
     * Проверка прав доступа для чата
     * Ларионов(разработчик): 149798103
     * Группа c Дмитрием Фоминым(заказчик), TapLinkBot: -1001232151616
     * Группа тестовая: -439603549
     *
     * @param request
     * @return
     */
    public boolean check(Request request) {

        return request.chatId.equals("-1001232151616") ||
                request.chatId.equals("-439603549") ||
                request.chatId.equals("149798103");
    }
}
