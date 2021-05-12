package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import taplinkbot.browser.Browser;
import taplinkbot.telegram.TelegramBot;

@Component
@RequiredArgsConstructor
public class PhoneNumberActions {

    protected final Browser browser;

    protected final TelegramBot telegram;

    /**
     * Установить новый номер телефона
     * Необходимо авторизоваться прежде чем использовать этот метод
     *
     * @param phoneNumber номер телефона
     * @throws Exception
     * @see AuthActions
     */
    public void setPhoneNumber(String phoneNumber) throws Exception {


        //@todo props data provider
        String url = "https://taplink.ru/";

        try {

            browser.setComment("Открытие страницы:" + url);
            browser.get(url);

            browser.setComment("Обращение к блоку WhatsUp.");
            WebElement we = browser.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[3]/div[3]/div/div/div/div[1]/div/div/div/div/div/div[7]/div/div/div[2]/div/div/a"));

            if (!we.isDisplayed()) {
                telegram.alert("Блок Whatsup не удалось найти по признаку displayed().", browser.takeScreenshot());
                throw new Exception("see telegram alerts");
            }

            if (!we.getText().equals("Узнать цену в WhatsApp")) {
                telegram.alert("Блок Whatsup не удалось найти по признаку getText().", browser.takeScreenshot());
                throw new Exception("see telegram alerts");
            }

            browser.setComment("Нажатие на блок WhatsUp");
            we.click();


            browser.setComment("Обращение к полю телефонного номера");
            we = browser.waitElement(By.xpath("/html/body/div[4]/div[2]/div/section/section[2]/div/div/div/div[2]/div[3]/div/div/input"));

            browser.setComment("Ввод телефонного номера");
            we.sendKeys("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
            we.sendKeys(phoneNumber);

            //@todo

            browser.setComment("Обращение к кнопке [Сохранить]");
            we = browser.waitElement(By.xpath("/html/body/div[4]/div[2]/div/footer/div[2]/button"));

            browser.setComment("Нажатие кнопки [Сохранить]");

            /** Главная кнопка! */
            we.click();


            browser.setComment("Ожидание Сохранения номера.");
            Thread.sleep(45 * 1000);

        } catch (Exception e) {
            telegram.alert("Установить номер не удалось.", browser.takeScreenshot());
            throw e;
        }
    }
}
