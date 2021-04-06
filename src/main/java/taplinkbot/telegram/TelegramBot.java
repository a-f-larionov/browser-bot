package taplinkbot.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import taplinkbot.schedulers.interavaled.Trigger;
import taplinkbot.service.StateService;

import javax.annotation.PostConstruct;

/**
 * Обработка телеграмм бота.
 * Парсит команды.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class TelegramBot extends TelegramLongPollingBot {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String botToken;

    private String infoChatId;

    private String alertChatId;

    @Autowired
    private Commands commands;

    @Autowired
    private Environment env;

    @Autowired
    private Trigger trigger;

    @Autowired
    private Parser parser;

    @Autowired
    private Accessor accessor;

    @Autowired
    private StateService stateService;

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

    public TelegramBot() {
        super();
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
            //@todo logger
            System.out.println(msg);

            if (!accessor.check(msg)) throw new ClientRequestException("Нет доступа");

            String[] args = Message.getFilledArgs(msg);

            stateService.setBotContext(msg.botContext);

            processCommand(msg.cammand, args[2], args[3], msg.chatId);

        } catch (ClientRequestException e) {
            System.out.println("Ошибка запроса пользователя." + e.getMessage() + " chatId:" + chatId);
            sendMessage(e.getMessage(), chatId);
        } finally {
            stateService.setBotContext(null);
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

        switch (command) {

            case "/help":
                commands.help(chatId);
                break;
            case "/start":
                commands.start(chatId, arg1);
                break;
            case "/stop":
                commands.stop(chatId);
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

            case "/set_manager_index":
                commands.setManagerIndex(arg1, chatId);
                break;
            case "/get_state":
                commands.getState(chatId);
                break;
            case "/check_date":

                sendMessage(
                        trigger.checkDate(arg1)
                        , chatId);
                break;

            default:
                sendMessage("Нужна другая команда.", chatId);
                break;
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
        System.out.println("ALERT: " + s);
        sendMessage(s, alertChatId);
    }

    public void alert(String s, String url) {
        System.out.println("ALERT: " + s + " " + url);
        sendMessage(s + " " + url, alertChatId);
    }

    public void info(String s) {
        sendMessage(s, infoChatId);
    }

    public void info(String s, String url) {
        sendMessage(s + " " + url, infoChatId);
    }

    protected void sendMessage(String text, String chatId) {
        System.out.println("Send message:`" + text + "` " + chatId);
        SendMessage m = new SendMessage();
        m.setChatId(chatId);
        m.setText(text);
        try {
            execute(m);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
