
package browserbot.services;

import browserbot.browser.Browser;
import browserbot.telegram.Reponse;
import browserbot.telegram.MessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.TimeoutException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExceptionHandler {

    private final Browser browser;

    /**
     * Обработка исключений происходит тут.
     * Это нужно для обработки исключений от бразуера
     *
     * @param e исключение
     * @return
     */
    public Reponse handle(Exception e) {

        System.err.println(e.getMessage());
        e.printStackTrace();

        // fix bug ERR_CONNECTION_CLOSED
        if (e.getMessage().equals("unknown error: net::ERR_CONNECTION_CLOSED") ||
                e instanceof TimeoutException
        ) {
            browser.resetBrowser();

            return MessageBuilder.buildInfo("Браузер не доступен, выполненна перезагрузка");
        }

        return MessageBuilder.buildAlert("Не удалось выполнить.", e);
    }
}
