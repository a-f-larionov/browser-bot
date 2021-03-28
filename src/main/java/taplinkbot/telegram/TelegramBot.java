package taplinkbot.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import taplinkbot.schedulers.interavaled.IntervaledTrigger;

/**
 * Обработка телеграмм бота.
 * Парсит команды.
 */
@Component
public class
TelegramBot extends TelegramLongPollingBot {

    private final String botToken;

    private final String infoChatId;

    private final String alertChatId;

    @Autowired
    private TelegramCommands commands;

    @Autowired
    Environment env;

    @Autowired
    private IntervaledTrigger trigger;

    @Autowired
    private Parser parser;

    @Autowired
    private Accessor accessor;

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
     * @todo вынести проверку прав
     * @todo вынести парсинг команды
     */
    private void processMessage(String text, String chatId) {

        Message message = parser.parse(text, chatId);

        if (!accessor.check(message)) {
            sendMessage("Привет, я бот, и доступен лишь ограниченым способам связи.", chatId);
            System.out.println("Сообщение не обработано`" + text + "` от:" + chatId);
            return;
        }

        if (message.args.length == 0) sendMessage("Неверная команда. Смотри /help", chatId);

        String[] args = Message.getPadArgs(message);
        
        processCommand(args[0], args[1], args[2], message.chatId);
    }

    private void processCommand(String command, String argument1, String argument2, String chatId) {

        switch (command) {

            case "/help":
                commands.help(chatId);
                break;
            case "/start":
                commands.start(chatId, argument1);
                break;
            case "/stop":
                commands.stop(chatId);
                break;
            case "/status":
                commands.status(chatId);
                break;
            case "/set_number":
                commands.setNumber(argument1, chatId);
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
                commands.holiDayAdd(chatId, argument1, argument2);
                break;

            case "/holidays_remove":
                commands.holiDayRemove(chatId, argument1);
                break;

            case "/holidays_list":
                commands.holidayList(chatId);
                break;


            case "/manager_list___":
                commands.managerList(chatId);
                break;

            case "/set_manager_index":
                commands.setManagerIndex(argument1, chatId);
                break;
            case "/get_state":
                commands.getState(chatId);
                break;
            case "/check_date":

                sendMessage(
                        trigger.checkDate(argument1)
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

    public TelegramBot(Environment env) {
        super();

        this.botToken = env.getProperty("telegram.lario.botToken");
        this.infoChatId = env.getProperty("telegram.lario.infoChatId");
        this.alertChatId = env.getProperty("telegram.lario.alertChatId");
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void alert(String s) {
        sendMessage(s, alertChatId);
    }

    public void alert(String s, String url) {
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
