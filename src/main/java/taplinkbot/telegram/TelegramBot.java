package taplinkbot.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import taplinkbot.bot.BotContexts;
import taplinkbot.schedulers.Trigger;

import javax.annotation.PostConstruct;

/**
 * Обработка телеграмм бота.
 * Парсит команды.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private String botToken;

    private String infoChatId;

    private String alertChatId;

    private Commands commands;

    private final Environment env;

    private final Trigger trigger;

    private final Parser parser;

    private final Accessor accessor;

    private final BotContexts botContexts;

    @PostConstruct
    public void init() {
        this.botToken = env.getProperty("telegram.botToken");
        this.infoChatId = env.getProperty("telegram.infoChatId");
        this.alertChatId = env.getProperty("telegram.alertChatId");

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            processMessage(
                    update.getMessage().getText(),
                    update.getMessage().getChatId().toString()
            );
        }
    }

    /**
     * Обработка сообщения.
     *
     * @param text
     * @param chatId
     * @todo вынести проверку прав паттерны?
     * @todo вынести парсинг команды паттерны?
     */
    private void processMessage(String text, String chatId) {

        try {
            Message msg = parser.parse(text, chatId);

            log.info(msg.toString());

            if (!accessor.check(msg)) throw new ClientRequestException("Нет доступа");

            String[] args = Message.getFilledArgs(msg);

            botContexts.setCurrent(msg.botContext);

            processCommand(msg.cammand, args[2], args[3], msg.chatId);

        } catch (ClientRequestException e) {
            log.info("Ошибка запроса пользователя." + e.getMessage() + " chatId:" + chatId);
            sendMessage(e.getMessage(), chatId);
        } finally {
            botContexts.setCurrent(null);
        }
    }

    /**
     * @param command
     * @param arg1
     * @param arg2
     * @param chatId
     * @todo spring command router?
     */
    private void processCommand(String command, String arg1, String arg2, String chatId) {

        try {
            String answer = null;

            switch (command) {

                case "/help":
                    answer = commands.help();
                    break;

                case "/start":
                    commands.start(chatId, arg1);
                    break;

                case "/stop":
                    commands.stop(chatId);
                    break;

                case "/restart":
                    commands.restart(chatId);
                    break;

                case "/status":
                    commands.status(chatId);
                    break;

                case "/set_number":
                    commands.setNumber(arg1, chatId);
                    break;

                case "/get_number":
                    commands.getNumber(chatId);
                    break;


                case "/everyday_allow":
                    commands.setEveryDay(chatId, true);
                    break;

                case "/everyday_disallow":
                    commands.setEveryDay(chatId, false);
                    break;

                case "/weekdays_allow":
                    commands.setAllowWeekDays(chatId, true);
                    break;

                case "/weekdays_disallow":
                    commands.setAllowWeekDays(chatId, false);
                    break;

                case "/weekends_allow":
                    commands.setAllowWeekEnds(chatId, true);
                    break;

                case "/weekends_disallow":
                    commands.setAllowWeekEnds(chatId, false);
                    break;

                case "/holidays_allow":
                    commands.setAllowHolidays(chatId, true);
                    break;

                case "/holidays_disallow":
                    commands.setAllowHolidays(chatId, false);
                    break;

                case "/holidays_add":
                    commands.holiDayAdd(chatId, arg1, arg2);
                    break;

                case "/holidays_remove":
                    commands.holiDayRemove(chatId, arg1);
                    break;

                case "/holidays_list":
                    commands.holidayList(chatId);
                    break;

                case "/manager_list___":
                    commands.managerList(chatId);
                    break;

                case "/manager_test":
                    commands.managerTest(chatId);
                    break;

                case "/set_manager_index":
                    commands.setManagerIndex(arg1);
                    break;

                case "/check_date":
                    sendMessage(
                            trigger.checkDate(arg1)
                            , chatId);
                    break;

                default:
                    //@todo multilanguage mechanizm realize
                    sendMessage("Не удалось распознать команду", chatId);
                    break;
            }

            if (answer != null) {
                sendMessage(answer, chatId);
            }

        } catch (Exception e) {
            sendMessage(e.getMessage(), chatId);
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

    public void alert(String s) {
        log.info("ALERT: " + s);
        sendMessage(s, alertChatId);
    }

    public void alert(String s, String url) {
        log.info("ALERT: " + s + " " + url);
        sendMessage(s + " " + url, alertChatId);
    }

    public void info(String s) {
        sendMessage(s, infoChatId);
    }

    public void info(String s, String url) {
        sendMessage(s + " " + url, infoChatId);
    }

    protected void sendMessage(String text, String chatId) {
        log.info("Send message:`" + text + "` " + chatId);
        SendMessage m = new SendMessage();
        m.setChatId(chatId);
        m.setText(text);
        try {
            execute(m);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setCommands(Commands commands) {
        this.commands = commands;
    }
}
