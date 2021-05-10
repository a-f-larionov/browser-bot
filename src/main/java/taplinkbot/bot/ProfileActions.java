package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import taplinkbot.browser.DriverWrapper;
import taplinkbot.telegram.TelegramBot;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileActions {

    protected final DriverWrapper browser;

    protected final TelegramBot telegram;

    public void changeTo(Profile profile) throws Exception {

        log.info("profile to " + profile.name);

        WebElement we;

        try {

            String url = "https://taplink.ru/profile/2988200/pages/";
            browser.comment("Открытие страницы:" + "https://taplink.ru/profile/2988200/pages/");
            browser.get(url);

            browser.comment("Обращение к всплывающему меню профиля");
            we = browser.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[2]/img"));
            we.click();

            //@todo for what?
            Thread.sleep(5000);

            browser.comment("Обращение к элементу `Мой профили`");
            we = browser.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[3]/div[1]/a"));
            if (!we.getText().equals("Мои профили")) {
                telegram.alert("Не удалось найти элемент. " + we.getText());
                throw new Exception("see telegram alerts");
            }
            we.click();

            browser.comment("Обращение к элементы заголовка профиля " + profile.getHtmlText() + ". Для проверки порядка профилей");

            final String xpath = "//td/div[contains(text(),'" + profile.getHtmlText() + "')]/..";
            we = browser.waitElement(By.xpath(xpath));

            if (we.getText().equals(profile.getHtmlText() + "\nТекущий профиль")) {
                return;
            }

            if (!we.getText().equals(profile.getHtmlText())) {
                telegram.alert("Не удалось найти элемент " + profile.getHtmlText() + ". " + we.getText());
                throw new Exception("see telegram alerts");
            }

            browser.comment("Обращение к элементы: кнопка переключение на профиль" + profile.getHtmlText());
            final String xpathButton = "//td/div[contains(text(),'" + profile.getHtmlText() + "')]/../../td/button";

            we = browser.waitElement(By.xpath(xpathButton));
            if (!we.getText().equals("Переключиться")) {
                telegram.alert("Не удалось найти элемент `переключиться`. " + we.getText());
                throw new Exception("see telegram alerts");
            }
            we.click();

            //@todo for what?
            Thread.sleep(5000);

        } catch (Exception e) {

            telegram.alert("Смена профиля не удалась.", browser.takeSreenshot());
            throw e;
        }
    }

}
