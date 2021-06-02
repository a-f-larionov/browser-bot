package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import taplinkbot.TapLinkBotException;
import taplinkbot.browser.Browser;
import taplinkbot.browser.BrowserException;
import taplinkbot.components.LangComponent;
import taplinkbot.telegram.TelegramBot;

@Component
@RequiredArgsConstructor
@Slf4j
public class MultiPageActions {

    private final LangComponent lang;

    private final TelegramBot telegram;

    private final Browser browser;

    @SneakyThrows
    public boolean checkPhoneNumber(Profile profile, String phoneNumber) {

        WebElement we;

        String url = profile.getPageUrl();

        if (!phoneNumber.matches("^\\+7\\d{10}$")) {
            throw new TapLinkBotException(lang.get("actions.phone_number_invalid", phoneNumber));
        }

        browser.setComment("Открытие страницы:" + url);
        browser.get(url);

        browser.setComment("Обращение к элементы 'узнать цену в WhatsApp'.");
        we = browser.waitElement(By.xpath("/html/body/div/div[3]/div/div[2]/div[2]/div/main/div/div/div/div/div/div/div[7]/div/div/div/div/a"));

        if (!we.getText().equals("Узнать цену в WhatsApp")) {
            throw new TapLinkBotException("Не нашелся блок Whatsup по признаку getText(), на странице \" + url");
        }
        if (!we.isDisplayed()) {
            throw new TapLinkBotException("Не нашелся блок Whatsup по признаку isDisplayed(), на странице " + url);
        }

        // удаляем знак '+' из номера
        String hrefExpect = "whatsapp://send?phone=" + phoneNumber.substring(1) + "&text=%D0%97%D0%B4%D1%80%D0%B0%D0%B2%D1%81%D1%82%D0%B2%D1%83%D0%B9%D1%82%D0%B5%21%20%D0%AF%20%D1%85%D0%BE%D1%87%D1%83%20%D1%83%D0%B7%D0%BD%D0%B0%D1%82%D1%8C%20%D1%86%D0%B5%D0%BD%D1%83%20%D0%BD%D0%B0%20%D0%BF%D0%BE%D1%80%D1%82%D1%80%D0%B5%D1%82.";
        String hrefFact = we.getAttribute("href");

        if (!hrefFact.equals(hrefExpect)) {
            throw new TapLinkBotException(
                    "Не нашелся блок Whatsup по признаку ссылки в href" +
                            "(ожидалось:'" + hrefExpect + "'," +
                            " фактически:'" + hrefFact + "')" +
                            ", на странице " + url);
        }

        we.click();

        browser.setComment("Ожидание нажатие на номер телефона.");
        Thread.sleep(45 * 1000);

        return true;
    }

    public String getNumber(Profile profile) {

        String url = profile.getPageUrl();

        WebElement we;

        browser.setComment("Открытие страницы:" + url);
        browser.get(url);

        browser.setComment("Обращение к элементы 'узнать цену в WhatsApp'.");
        //we = browser.waitElement(By.xpath("/html/body/div/div[3]/div/div[2]/div[2]/div/main/div/div/div/div/div/div/div[7]/div/div/div/div/a"));
        we = browser.waitElement(By.xpath("//span[contains(text(),'Узнать цену в WhatsApp')]/.."));

        if (!we.getText().equals("Узнать цену в WhatsApp")) {
            throw new BrowserException("Не нашелся блок Whatsup по признаку getText(), на странице " + url, browser);
        }
        if (!we.isDisplayed()) {
            throw new BrowserException("Не нашелся блок Whatsup по признаку isDisplayed(), на странице " + url, browser);
        }

        /* Удаляем знак '+' из номера */
        String hrefFact = we.getAttribute("href");

        hrefFact = hrefFact.replace("whatsapp://send?phone=", "");
        hrefFact = hrefFact.replace("&text=%D0%97%D0%B4%D1%80%D0%B0%D0%B2%D1%81%D1%82%D0%B2%D1%83%D0%B9%D1%82%D0%B5%21%20%D0%AF%20%D1%85%D0%BE%D1%87%D1%83%20%D1%83%D0%B7%D0%BD%D0%B0%D1%82%D1%8C%20%D1%86%D0%B5%D0%BD%D1%83%20%D0%BD%D0%B0%20%D0%BF%D0%BE%D1%80%D1%82%D1%80%D0%B5%D1%82.", "");

        return hrefFact;
    }

}
