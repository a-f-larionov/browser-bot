package taplinkbot.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.TimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import taplinkbot.browser.Browser;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * Телеграм бот.
 * Принимает и оптравляет сообщения
 *
 * @link https://core.telegram.org/bots/samples
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.botToken}")
    private String botToken;

    @Value("${telegram.infoChatId}")
    private String infoChatId;

    @Value("${telegram.alertChatId}")
    private String alertChatId;

    private final CommandProccesor commandProccesor;

    private final Browser browser;

    /**
     * @link https://core.telegram.org/bots/samples
     */
    @PostConstruct
    public void init() {

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "I am the Tap Link Bot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    /**
     * @param update
     * @link https://core.telegram.org/bots/samples
     */
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String chatId = update.getMessage().getChatId().toString();
            String message = update.getMessage().getText();

            try {
                commandProccesor.processTelegramMessage(message, chatId);

            } catch (Exception e) {

                sendMessage(e.getMessage(), chatId);
            }
        }
    }

    public void sendMessage(String message, String chatId) {

        log.info("Send message:`" + message + "` " + chatId);

        SendMessage m = new SendMessage(chatId, message);

        try {
            execute(m);
        } catch (TelegramApiException e) {

            log.warn(e.getMessage());
            e.printStackTrace();
        }
    }

    public void alert(String msg) {
        log.info(msg);
        sendMessage(msg, "149798103");
        sendMessage(msg, alertChatId);
    }

    public void alert(String s, String url) {
        String msg = "ALERT: " + s + " " + url;
        alert(msg);
    }

    public void info(String message) {
        sendMessage(message, infoChatId);
    }

    public void info(String message, String url) {
        sendMessage(message + " " + url, infoChatId);
    }

    /**
     * Отправить сообщение.
     *
     * @param request
     * @param message
     */
    public void notify(Request request, Message message) {
        String infoChatId, alertChatId;

        // определим адресацию собщений
        if (request.initiatorChatId != null) {
            infoChatId = alertChatId = request.initiatorChatId;
        } else {
            infoChatId = this.infoChatId;
            alertChatId = this.alertChatId;
        }

        alertChatId = "149798103";

        // отправим сообщение
        switch (message.getType()) {
            case ALERT:
                if (message.getException() != null) {
                    message.getException().printStackTrace();

                    log.info(message.getException().getMessage());
                }
                //org.openqa.selenium.TimeoutException: timeout: Timed out receiving message from renderer: 10.000
                if (message.getException() instanceof TimeoutException) {

                    browser.fixBugErrConnectionClosed();
                }
                if (message.getException() != null && message.getException().getMessage()
                        .equals("unknown error: net::ERR_CONNECTION_CLOSED")) {

                    browser.fixBugErrConnectionClosed();
                }

                sendMessage(message.getDescription() + "  " + browser.takeScreenshot(), alertChatId);

                break;

            case INFO:
                sendMessage(message.getDescription(), infoChatId);
                break;

            case RESULT:
                if (request.initiatorChatId != null) {
                    sendMessage(message.getDescription(), request.initiatorChatId);
                } else {
                    //log.info(message.getDescription());
                    //sendMessage(message.getDescription(), "149798103");
                }
                break;
        }
    }
}
