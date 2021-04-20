package taplinkbot.telegram;

import org.springframework.stereotype.Component;

@Component
public class Accessor {

    /**
     * Проверка прав доступа для чата
     * Ларионов(разработчик): 149798103
     * Группа c Дмитрием Фоминым(заказчик), TapLinkBot: -1001232151616
     * Группа тестовая: -439603549
     *
     * @param message
     * @return
     */
    public boolean check(Message message) {

        return message.chatId.equals("-1001232151616") ||
                message.chatId.equals("-439603549") ||
                message.chatId.equals("149798103");
    }
}
