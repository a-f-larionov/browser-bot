package taplinkbot.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import taplinkbot.bot.CanvasRuComActions;
import taplinkbot.managers.Manager;
import taplinkbot.managers.ManagerRotator;
import taplinkbot.schedulers.interavaled.Trigger;
import taplinkbot.service.HolidayService;
import taplinkbot.service.StateService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
public class Commands {

    @Autowired
    private TelegramBot telegram;

    private final ManagerRotator managerRotator;

    private final StateService stateService;

    private final CanvasRuComActions canvasRuComActions;

    private final HolidayService holidayService;

    private final Trigger trigger;

    private boolean functionalHolidays = false;

    public void start(String chatId, String argument) {

        if (argument.equals(Message.noArgumentValue)) {
            stateService.schedulerSetActive(true);
            sayStatus(chatId);
        } else {

            try {
                long minutes = Long.parseLong(argument);
                System.out.println(minutes);
                System.out.println("argument" + argument);

                ExecutorService service = Executors.newCachedThreadPool();
                telegram.sendMessage("Расписание будет запущено, через " + minutes + " минут(у,ы).", chatId);
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(minutes * 60 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        telegram.info("Расписание запущено по таймаута. Запрос минут назад: " + minutes);
                        System.out.println("finish");
                        stateService.schedulerSetActive(true);
                    }
                });

            } catch (NumberFormatException e) {
                telegram.sendMessage("Не верный аргумент, " +
                        "должно быть целлое число минут. Передано `" + argument + "`", chatId);
            }
        }
    }

    public void stop(String chatId) {
        stateService.schedulerSetActive(false);
        sayStatus(chatId);
    }

    public void status(String chatId) {
        sayStatus(chatId);
    }

    public void setNumber(String phoneNumber, String chatId) {
        if (!phoneNumber.matches("^\\+7\\d{10}$")) {
            telegram.sendMessage("Номер телефона должен быть в формате +71234567890, передано:'" + phoneNumber + "'", chatId);
            return;
        }

        telegram.sendMessage("Начинаю смену номера:" + phoneNumber, chatId);
        canvasRuComActions.authAndUpdatePhone(phoneNumber, true, true);
    }

    public void getNumber(String chatId) {

        String phoneNumber = null;
        try {

            phoneNumber = canvasRuComActions.getNumber();
            telegram.sendMessage("Номер телефона: " + phoneNumber, chatId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sayStatus(String chatId) {
        String message;

        message = "\r\nРасписание: \t" + (stateService.schedulerIsActive() ? "включено" : "выключено");
        message += "\r\nВО ВСЕ ДНИ без исключения: \t" + (stateService.allowEveryDay() ? "да" : "нет");
        message += "\r\nВ будние дни: \t\t\t" + (stateService.allowWeekDays() ? "да" : "нет");
        message += "\r\nВ быходные дни: \t\t\t" + (stateService.allowWeekEnds() ? "да" : "нет");

        if (functionalHolidays)
            message += "\r\nВ праздники(не рабочие):\t\t" + (stateService.allowHoliDays() ? "да" : "нет");

        telegram.sendMessage(message, chatId);
    }

    public void getState(String chatId) {
        if (chatId.equals("149798103")) {
            Calendar c = Calendar.getInstance();
            String msg = "";
            msg += " \r\ninterval: " + stateService.getManagerInterval();
            msg += " \r\nlast timestamp: " +
                    new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(stateService.getIntervalledLastTimestamp());
            msg += " \r\nscheduler is active: " + stateService.schedulerIsActive();
            msg += " \r\nlast index: " + stateService.getManagerLastIndex();
            msg += " \r\nis every day: " + (stateService.allowEveryDay() ? "y" : "n");

            msg += "\r\n";
            msg += "\r\n elapsed time: " + trigger.getElapsedTime(c.getTimeInMillis());

            msg += "\r\n inFromTo: " + (trigger.allowRun() ? "y" : "n");
            msg += "\r\n timeIsLeft: " + (trigger.isIntervalLeft(c.getTimeInMillis()) ? "y" : "n");
            msg += "\r\n isFittedInterval: " + (trigger.isBeginOfInterval(c.getTimeInMillis()) ? "y" : "n");

            telegram.sendMessage(msg, chatId);
        } else {
            System.out.println("get_state from:" + chatId);
        }
    }

    public void setManagerIndex(String argument, String chatId) {
        switch (argument) {
            case "0":
                managerRotator.setIndex(0);
                break;
            case "1":
                managerRotator.setIndex(1);
                break;
            case "2":
                managerRotator.setIndex(2);
                break;
        }
    }

    public void setEveryDay(String chatId, boolean value) {
        stateService.setIsEveryDay(value);
        sayStatus(chatId);
    }

    public void setAllowWeekDays(String chatId, boolean value) {
        stateService.setAllowWeekDays(value);
        sayStatus(chatId);
    }

    public void setAllowWeekEnds(String chatId, boolean value) {
        stateService.setAllowWeekEnds(value);
        sayStatus(chatId);
    }

    public void setAllowHolidays(String chatId, boolean value) {
        if (!functionalHolidays) return;
        stateService.setAllowHolidays(value);
        sayStatus(chatId);
    }

    public void help(String chatId) {
        telegram.sendMessage(
                "/help - подсказка\r\n" +

                        "/start - Запустить работу расписания /start\r\n" +
                        "/stop - Остановить работу расписания /stop\r\n" +
                        "/status - Узнать состояние бота /status\r\n" +

                        "/set_number - Установить номер /set_number +71234567890\r\n" +

                        "/everyday_allow - работать все дни\r\n" +
                        "/everyday_disallow - работать не все дни\r\n" +

                        "/weekends_allow - работать все выходные дни\r\n" +
                        "/weekends_disallow - не работать в выходные дни\r\n" +

                        "/holidays_allow - работать в праздники\r\n" +
                        "/holidays_disallow - не работать в праздники\r\n" +

                        "/holidays_add - добавить не рабочий день \r\n" +
                        "/holidays_remove - удалить не рабочий день \r\n" +
                        "/holidays_list - получить список не рабоичих дней\r\n" +

                        "",

                chatId
        );
    }

    public void holiDayAdd(String chatId, String dateText, String comment) {
        if (!functionalHolidays) return;
        String[] args = dateText.split("\\.");
        int month, day, year;
        try {
            if (args.length != 3) throw new Exception("Передан не верный аргумент");
            day = Integer.parseInt(args[0]);
            month = Integer.parseInt(args[1]);
            year = Integer.parseInt(args[2]);
            holidayService.add(day, month - 1, year, comment);
            telegram.sendMessage("Добавлено, день: " + day + ", месяц:" + month + ", год:" + year, chatId);
        } catch (Exception e) {
            telegram.sendMessage("Не верный аргумент. Нужно передать '29.12.2021' день и месяц, год, передано: `" + dateText + "`", chatId);
        }
        holidayList(chatId);
    }

    public void holidayList(String chatId) {
        if (!functionalHolidays) return;
        String msg;
        msg = holidayService.getInfoText();
        if (msg.isEmpty()) msg = "Нет дней.";
        telegram.sendMessage(msg, chatId);
    }

    public void holiDayRemove(String chatId, String argument) {
        if (!functionalHolidays) return;
        try {
            int id = Integer.parseInt(argument);
            holidayService.remove(id);
        } catch (Exception e) {
            telegram.sendMessage("нужно передать id не рабочего дня.", chatId);
        }
        holidayList(chatId);
    }

    public void managerList(String chatId) {

        String msg;
        msg = "";

        Manager[] managers = managerRotator.getList();

        for (int i = 0; i < managers.length; i++) {
            msg += i + " - " + managers[i].getDescription() + "\r\n";
        }

        telegram.sendMessage(msg, chatId);
    }
}