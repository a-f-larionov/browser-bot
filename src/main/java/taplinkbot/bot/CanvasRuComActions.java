package taplinkbot.bot;

import taplinkbot.entities.PhoneLogger;
import taplinkbot.repositories.PhoneLoggerRepository;
import taplinkbot.telegram.TelegramBot;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class CanvasRuComActions {

    @Autowired
    private Semaphore semaphore;

    @Autowired
    private TelegramBot telegram;

    @Autowired
    private DriverWrapper wrapper;

    @Autowired
    private PhoneLoggerRepository phoneLoggerRepository;

    @Autowired
    private Environment env;

    private WebElement we;

    String canvasRuComUrl = "https://canvas.ru.com/";

    public void authAndUpdatePhone(String phoneNumber, boolean stepsInfo, boolean imShure) {

        if (!phoneNumber.matches("^\\+7\\d{10}$")) {
            telegram.info("Номер телефона должен быть в формате +71234567890, передано:'" + phoneNumber + "'");
            return;
        }

        //if (stepsInfo && semaphore.isntLocked()) telegram.info("Запускаю установку номера:" + phoneNumber);

        try {
            if (semaphore.lock()) {

                if (stepsInfo) telegram.info("Авторизация");
                authorize();
                if (stepsInfo) telegram.info("Авторизация успешна", wrapper.takeSreenshot());

                if (stepsInfo) telegram.info("Смена профиля");
                changeProfileToCanvasRuCom();

                if (stepsInfo) telegram.info("Установка номера" + phoneNumber);
                setPhoneNumber(phoneNumber, imShure);

                if (stepsInfo) telegram.info("Проверка страницы TapLink CanvasRuCom");
                checkThePage(phoneNumber);
                if (stepsInfo) telegram.info("Проверка завершена", wrapper.takeSreenshot());
            } else {
                telegram.alert("Бот заблокирован, попробуйте позже.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            telegram.alert("Не удалось выполнить действие.", wrapper.takeSreenshot());
        } finally {
            semaphore.unlock();
        }
    }

    private void changeProfileToCanvasRuCom() throws Exception {

        try {

            String url = "https://taplink.ru/profile/2988200/pages/";
            wrapper.humanComment("Открытие страницы:" + "https://taplink.ru/profile/2988200/pages/");
            wrapper.get(url);

            wrapper.humanComment("Обращение к всплывающему меню профиля");
            we = wrapper.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[2]/img"));
            we.click();

            Thread.sleep(5000);

            wrapper.humanComment("Обращение к элементу `Мой профили`");
            we = wrapper.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[3]/div[1]/a"));
            if (!we.getText().equals("Мои профили")) {
                telegram.alert("Не удалось найти элемент. " + we.getText());
                throw new Exception("see telegram alerts");
            }
            we.click();

            wrapper.humanComment("Обращение к элементы заголовка профиля canvas ru com. Для проверки порядка профилей");
            //we = wrapper.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[3]/div[2]/div/div[2]/table/tbody/tr[3]/td[1]"));
            we = wrapper.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[3]/div[2]/div/div[2]/table/tbody/tr[3]/td[1]"));

            if (we.getText().equals("canvas.ru.com\nТекущий профиль")) {
                //telegram.info("Профиль canvas.ru.com УЖЕ является текущем.");
                return;
            }

            if (!we.getText().equals("canvas.ru.com")) {
                telegram.alert("Не удалось найти элемент canvas.ru.com. " + we.getText());
                throw new Exception("see telegram alerts");
            }

            wrapper.humanComment("Обращение к элементы: кнопка переключение на профиль canvas ru com");
            we = wrapper.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[3]/div[2]/div/div[2]/table/tbody/tr[3]/td[2]/button"));
            if (!we.getText().equals("Переключиться")) {
                telegram.alert("Не удалось найти элемент `переключиться`. " + we.getText());
                throw new Exception("see telegram alerts");
            }
            we.click();
            telegram.info("Профиль canvas.ru.com ТЕПЕРЬ является текущий.");

            Thread.sleep(5000);

        } catch (Exception e) {
            telegram.alert("Авторизация не удалась.", wrapper.takeSreenshot());
            throw e;
        }
    }

    private void authorize() throws Exception {
        try {

            wrapper.reset();

            String url = "https://taplink.ru/profile/auth/signin/";
            wrapper.humanComment("Открытие страницы:" + url);
            wrapper.get(url);

            enterLogin();

            enterPassword();

            authSubmit();

        } catch (Exception e) {
            telegram.alert("Авторизация не удалась.", wrapper.takeSreenshot());
            throw e;
        }
    }

    private void enterLogin() {
        wrapper.humanComment("Обращение к полю ввода логина");
        we = wrapper.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/div[1]/div/input"));

        wrapper.humanComment("Ввод логина");
        we.sendKeys(env.getProperty("taplink.canvas.username"));
    }

    private void enterPassword() {
        wrapper.humanComment("Обращение к поле ввода пароля");
        we = wrapper.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/div[2]/div[2]/input"));

        wrapper.humanComment("Ввод пароля");
        we.sendKeys(env.getProperty("taplink.canvas.password"));
    }

    private void authSubmit() {
        wrapper.humanComment("Обращение к кнопки авторизации");
        we = wrapper.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/button"));

        wrapper.humanComment("Нажатие кнопки авторизации");
        we.click();

        wrapper.humanComment("Проверка начилия иконки профиля(проверка авторизации)");
        wrapper.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[2]/img"));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void setPhoneNumber(String phoneNumber, boolean imShure) throws Exception {

        try {
            String url = "https://taplink.ru/";
            wrapper.humanComment("Открытие страницы:" + url);
            wrapper.get(url);

            wrapper.humanComment("Обращение к блоку WhatsUp.");
            we = wrapper.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[3]/div[3]/div/div/div/div[1]/div/div/div/div/div/div[7]/div/div/div[2]/div/div/a"));

            if (!we.isDisplayed()) {
                telegram.alert("Блок Whatsup не удалось найти по признаку displayed().", wrapper.takeSreenshot());
                throw new Exception("see telegram alerts");
            }

            if (!we.getText().equals("Узнать цену в WhatsApp")) {
                telegram.alert("Блок Whatsup не удалось найти по признаку getText().", wrapper.takeSreenshot());
                throw new Exception("see telegram alerts");
            }

            wrapper.humanComment("Нажатие на блок WhatsUp");
            we.click();


            wrapper.humanComment("Обращение к полю телефонного номера");
            we = wrapper.waitElement(By.xpath("/html/body/div[4]/div[2]/div/section/section[2]/div/div/div/div[2]/div[3]/div/div/input"));

            wrapper.humanComment("Ввод телефонного номера");
            we.sendKeys("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
            we.sendKeys(phoneNumber);

            //@todo

            wrapper.humanComment("Обращение к кнопке [Сохранить]");
            we = wrapper.waitElement(By.xpath("/html/body/div[4]/div[2]/div/footer/div[2]/button"));

            wrapper.humanComment("Нажатие кнопки [Сохранить]");

            //Главная кнопка!
            if (imShure) {
                we.click();
            }

            wrapper.humanComment("Ожидание Сохранения номера.");
            Thread.sleep(45 * 1000);

        } catch (Exception e) {
            telegram.alert("Установить номер не удалось.", wrapper.takeSreenshot());
            throw e;
        }
    }

    private void checkThePage(String phoneNumber) throws Exception {

        try {
            if (!phoneNumber.matches("^\\+7\\d{10}$")) {
                telegram.alert("Номер телефона должен быть в формате +71234567890, передано:'" + phoneNumber + "'");
                throw new Exception("see telegram alerts");
            }

            wrapper.humanComment("Открытие страницы:" + canvasRuComUrl);
            wrapper.get(canvasRuComUrl);

            wrapper.humanComment("Обращение к элементы 'узнать цену в WhatsApp'.");
            we = wrapper.waitElement(By.xpath("/html/body/div/div[3]/div/div[2]/div[2]/div/main/div/div/div/div/div/div/div[7]/div/div/div/div/a"));

            if (!we.getText().equals("Узнать цену в WhatsApp")) {
                telegram.alert("Не нашелся блок Whatsup по признаку getText(), на странице " + canvasRuComUrl, wrapper.takeSreenshot());
                throw new Exception("see telegram alerts");
            }
            if (!we.isDisplayed()) {
                telegram.alert("Не нашелся блок Whatsup по признаку isDisplayed(), на странице " + canvasRuComUrl, wrapper.takeSreenshot());
                throw new Exception("see telegram alerts");
            }

            // удаляем знак '+' из номера
            String hrefExpect = "whatsapp://send?phone=" + phoneNumber.substring(1) + "&text=%D0%97%D0%B4%D1%80%D0%B0%D0%B2%D1%81%D1%82%D0%B2%D1%83%D0%B9%D1%82%D0%B5%21%20%D0%AF%20%D1%85%D0%BE%D1%87%D1%83%20%D1%83%D0%B7%D0%BD%D0%B0%D1%82%D1%8C%20%D1%86%D0%B5%D0%BD%D1%83%20%D0%BD%D0%B0%20%D0%BF%D0%BE%D1%80%D1%82%D1%80%D0%B5%D1%82.";
            String hrefFact = we.getAttribute("href");

            if (!hrefFact.equals(hrefExpect)) {
                telegram.alert("Не нашелся блок Whatsup по признаку ссылки в href" +
                        "(ожидалось:'" + hrefExpect + "'," +
                        " фактически:'" + hrefFact + "')" +
                        ", на странице " + canvasRuComUrl, wrapper.takeSreenshot());
                throw new Exception("see telegram alerts");
            }

            we.click();

            wrapper.humanComment("Ожидание нажатие на номер телефона.");
            Thread.sleep(45 * 1000);

            telegram.info("Номер на странице " + canvasRuComUrl + " соответствует:" + phoneNumber, wrapper.takeSreenshot());

        } catch (Exception e) {
            e.printStackTrace();
            telegram.alert("Ну удалось проверить страницу ТапЛинк `" + canvasRuComUrl + "` ,последние действие:" + wrapper.getHumanComment(), wrapper.takeSreenshot());
            throw e;
        }
    }

    public void checkCanvas() {


        try {
            String phone = getNumber();

            phoneLoggerRepository.save(new PhoneLogger(phone));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getNumber() throws Exception {

        try {
            if (semaphore.lock()) {
                wrapper.humanComment("Открытие страницы:" + canvasRuComUrl);
                wrapper.get(canvasRuComUrl);

                wrapper.humanComment("Обращение к элементы 'узнать цену в WhatsApp'.");
                we = wrapper.waitElement(By.xpath("/html/body/div/div[3]/div/div[2]/div[2]/div/main/div/div/div/div/div/div/div[7]/div/div/div/div/a"));

                if (!we.getText().equals("Узнать цену в WhatsApp")) {
                    telegram.alert("Не нашелся блок Whatsup по признаку getText(), на странице " + canvasRuComUrl, wrapper.takeSreenshot());
                    throw new Exception("see telegram alerts");
                }
                if (!we.isDisplayed()) {
                    telegram.alert("Не нашелся блок Whatsup по признаку isDisplayed(), на странице " + canvasRuComUrl, wrapper.takeSreenshot());
                    throw new Exception("see telegram alerts");
                }

                // удаляем знак '+' из номера
                String hrefFact = we.getAttribute("href");

                hrefFact = hrefFact.replace("whatsapp://send?phone=", "");
                hrefFact = hrefFact.replace("&text=%D0%97%D0%B4%D1%80%D0%B0%D0%B2%D1%81%D1%82%D0%B2%D1%83%D0%B9%D1%82%D0%B5%21%20%D0%AF%20%D1%85%D0%BE%D1%87%D1%83%20%D1%83%D0%B7%D0%BD%D0%B0%D1%82%D1%8C%20%D1%86%D0%B5%D0%BD%D1%83%20%D0%BD%D0%B0%20%D0%BF%D0%BE%D1%80%D1%82%D1%80%D0%B5%D1%82.", "");
                return hrefFact;
            } else {
                telegram.alert("Бот заблокирован, попробуйте позже.");
                return "Бот заблокирован, попробуйте позже.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            telegram.alert("Ну удалось получить номер со страницы ТапЛинк `" + canvasRuComUrl + "` ,последние действие:" + wrapper.getHumanComment(), wrapper.takeSreenshot());
            throw e;
        } finally {
            semaphore.unlock();
        }
    }
}
