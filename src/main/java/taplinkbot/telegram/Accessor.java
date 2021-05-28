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
     * @param req
     * @return
     */
    public boolean check(Request req) {

        return req.skipCheckRights ||
                req.initiatorChatId.equals("-1001232151616") ||
                req.initiatorChatId.equals("-439603549") ||
                req.initiatorChatId.equals("149798103");
    }
}
