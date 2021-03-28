package taplinkbot.bot;

import taplinkbot.telegram.TelegramBot;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LarioActions {

    @Autowired
    private Semaphore semaphore;

    @Autowired
    private TelegramBot telegram;

    @Autowired
    DriverWrapper wrapper;

    @Autowired
    Environment env;

    WebElement we;

    public void authAndUpdatePhone(String phoneNumber, boolean stepInfo) {
        if (!phoneNumber.matches("^\\+7\\d{10}$")) {
            telegram.info("Номер телефона должен быть в формате +71234567890, передано:'" + phoneNumber + "'");
            return;
        }

        if (semaphore.isntLocked()) telegram.info("Запускаю установку номера" + phoneNumber);

        try {
            if (semaphore.lock()) {
                if (stepInfo) telegram.info("Авторизация");
                authorize();

                if (stepInfo) telegram.info("Установка номера" + phoneNumber);
                setPhoneNumber(phoneNumber);

                if (stepInfo) telegram.info("Проверка страницы TapLink larionov");
                checkThePage(phoneNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
            telegram.alert("Не удалось выполнить тест.", wrapper.takeSreenshot());
        } finally {
            semaphore.unlock();
        }
    }

    private void authorize() {
        try {

            wrapper.reset();

            wrapper.get("https://taplink.ru/profile/auth/signin/");

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
        we.sendKeys(env.getProperty("taplink.lario.username"));
    }

    private void enterPassword() {
        wrapper.humanComment("Обращение к поле ввода пароля");
        we = wrapper.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/div[2]/div[2]/input"));

        wrapper.humanComment("Ввод пароля");
        we.sendKeys(env.getProperty("taplink.lario.password"));
    }

    private void authSubmit() {
        wrapper.humanComment("Обращение к кнопки авторизации");
        we = wrapper.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/button"));

        wrapper.humanComment("Нажатие кнопки авторизации");
        we.click();

        wrapper.humanComment("Проверка начилия иконки профиля(проверка авторизации)");
        wrapper.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[2]/img"));

    }

    public void setPhoneNumber(String phoneNumber) throws Exception {

        try {
            wrapper.get("https://taplink.ru/");

            wrapper.humanComment("Обращение к блоку WhatsUp.");
            we = wrapper.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[3]/div[3]/div/div/div/div[1]/div/div/div/div[2]/div/div/a"), 10);

            if (!we.isDisplayed()) {
                telegram.alert("Блок Whatsup не удалось найти по признаку displayed().", wrapper.takeSreenshot());
                throw new Exception("see telegram alerts");
            }

            if (!we.getText().equals("WhatsUp\nphone")) {
                telegram.alert("Блок Whatsup не удалось найти по признаку getText().", wrapper.takeSreenshot());
                throw new Exception("see telegram alerts");
            }

            wrapper.humanComment("Нажатие на блок WhatsUp");
            we.click();

            wrapper.humanComment("Обращение к полю телефонного номера");
            we = wrapper.waitElement(By.xpath("/html/body/div[4]/div[2]/div/section/section[1]/div[2]/div/div/div[2]/div/div/input"));

            wrapper.humanComment("Ввод телефонного номера");
            we.sendKeys("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
            we.sendKeys(phoneNumber);
            //input номера      html/body/div[4]/div[2]/div/section/section[1]/div[2]/div/div/div[2]/div/div/input
            //@todo

            wrapper.humanComment("Обращение к кнопке [Сохранить]");
            we = wrapper.waitElement(By.xpath("/html/body/div[4]/div[2]/div/footer/div[2]/button"));

            wrapper.humanComment("Нажатие кнопки [Сохранить]");
            we.click();

            Thread.sleep(10 * 1000);

        } catch (Exception e) {
            telegram.alert("Установить номер не удалось.", wrapper.takeSreenshot());
            throw e;
        }
    }


    public void checkThePage(String phoneNumber) throws Exception {
        String taplinkUrl = "https://taplink.cc/a.f.larionov";

        try {
            if (!phoneNumber.matches("^\\+7\\d{10}$")) {
                telegram.alert("Номер телефона должен быть в формате +71234567890, передано:'" + phoneNumber + "'");
                throw new Exception("see telegram alerts");
            }

            wrapper.get(taplinkUrl);

            we = wrapper.waitElement(By.xpath("/html/body/div/div[3]/div/div[2]/div[2]/div/main/div/div/div/div/div/div/div/div[2]/div/a"));
            if (!we.getText().equals("WhatsUp\nphone")) {
                telegram.alert("Не нашелся блок Whatsup по признаку getText(), на странице " + taplinkUrl, wrapper.takeSreenshot());
                throw new Exception("see telegram alerts");
            }
            if (!we.isDisplayed()) {
                telegram.alert("Не нашелся блок Whatsup по признаку isDisplayed(), на странице " + taplinkUrl, wrapper.takeSreenshot());
                throw new Exception("see telegram alerts");
            }

            String hrefExpect = "tel:" + phoneNumber;
            if (!we.getAttribute("href").equals(hrefExpect)) {
                telegram.alert("Не нашелся блок Whatsup по признаку номер телефона в href(" + hrefExpect + "), на странице " + taplinkUrl, wrapper.takeSreenshot());
                throw new Exception("see telegram alerts");
            }

            we.click();

            //wrapper.mouseMove(we);

            Thread.sleep(1000);

            telegram.info("Номер на странице " + taplinkUrl + ", соответствует:" + phoneNumber, wrapper.takeSreenshot());

        } catch (Exception e) {

            telegram.alert("Ну удалось проверить страницу ТапЛинк " + taplinkUrl, wrapper.takeSreenshot());
            throw e;
        }
    }
}
