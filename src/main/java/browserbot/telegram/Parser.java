package browserbot.telegram;

import org.springframework.stereotype.Component;
import browserbot.BrowserBotException;
import browserbot.bots.taplink.Profile;

//@Todo
@Component
public class Parser {

    /**
     * Распарсириует команду от телеграм бота
     *
     * @param telegramText текст команды с аргументами
     * @return CommandRequest
     */
    public Request parse(String telegramText) {

        telegramText = prepareText(telegramText);

        String[] parts = parseParts(telegramText);

        if (parts.length == 0) throw new BrowserBotException("Неверная команда. см. /help");

        if (parts.length < 2) {
            throw new BrowserBotException("Не указан аргумент кабинета. /command [кабинет]." + Profile.getListOfAll());
        }

        Profile profile = Profile.searchByName(parts[1]);

        if (profile == null) {
            throw new BrowserBotException("Не удалось определить кабинет." + Profile.getListOfAll());
        }

        return buildCommandRequest(parts, profile);
    }

    private Request buildCommandRequest(String[] parts, Profile profile) {
        Request request = new Request();

        // собираем запрос на команду
        request.command = parts[0];
        request.profile = profile;

        if (parts.length > 2) request.arg1 = parts[2];
        if (parts.length > 3) request.arg2 = parts[3];

        return request;
    }


    /**
     * Делим текст на части.
     *
     * @param telegramText
     * @return
     */
    private String[] parseParts(String telegramText) {
        return telegramText.split(" ");
    }

    /**
     * Удаляем не лишние.
     *
     * @param telegramText
     * @return
     */
    private String prepareText(String telegramText) {
        // удаляеям @tap_link_bot
        telegramText = telegramText.replace("@tap_link_bot", "");

        // удаляем лишние пробелы
        telegramText = telegramText
                .replace("  ", " ")
                .replace("  ", " ")
                .replace("  ", " ")
                .replace("  ", " ");

        return telegramText;
    }
}
