package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Предоставляет данные для работы с taplink.ru
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataProvider {

    protected final BotContexts botContexts;

    /**
     * Возвращает ссылку на страницу Мультиссылки.
     *
     * @return мультиссылка
     */
    public String getPageUrl() {
        return "https://" + botContexts.current().profile.getDomainName() + "/";
    }
}
