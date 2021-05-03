package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import taplinkbot.browser.DriverWrapper;
import taplinkbot.entities.PhoneLogger;
import taplinkbot.repositories.PhoneLoggerRepository;
import taplinkbot.service.StateService;
import taplinkbot.telegram.TelegramBot;

@Component
@RequiredArgsConstructor
@Slf4j
public class Actions {

    protected final TelegramBot telegram;

    protected final DriverWrapper browser;

    protected final StateService stateService;

    protected final BotContexts botContexts;

    protected final PhoneLoggerRepository phoneLoggerRepository;

    protected final TapLinkAuthActions tapLinkAuthActions;

    protected final Environment env;

    private WebElement we;

    synchronized public void authAndUpdatePhone(String phoneNumber, boolean stepsInfo, boolean imShure) {

        assert phoneNumber != null;

        if (!phoneNumber.matches("^\\+7\\d{10}$")) {
            telegram.info("Номер телефона должен быть в формате +71234567890, передано:'" + phoneNumber + "'");
            return;
        }

        try {

            if (stepsInfo) telegram.info("Авторизация");

            tapLinkAuthActions.authorize();

            if (stepsInfo) telegram.info("Авторизация успешна", browser.takeSreenshot());

            if (stepsInfo) telegram.info("Смена профиля");

            //@todo taplinkProfileActions
            changeProfile(botContexts.getCurrent().profile);

            //@todo taplinkPhoneActions
            if (stepsInfo) telegram.info("Установка номера" + phoneNumber);
            setPhoneNumber(phoneNumber, imShure);

            if (stepsInfo) telegram.info("Проверка страницы TapLink" + botContexts.getCurrent().name);

            //@todo taplinkPageActions
            checkThePage(phoneNumber);
            if (stepsInfo) telegram.info("Проверка завершена", browser.takeSreenshot());
        } catch (Exception e) {
            e.printStackTrace();
            telegram.alert("Не удалось выполнить действие.", browser.takeSreenshot());
        }
    }

    private void changeProfile(Profile profile) throws Exception {

        assert profile != null;

        try {

            String url = "https://taplink.ru/profile/2988200/pages/";
            browser.humanComment("Открытие страницы:" + "https://taplink.ru/profile/2988200/pages/");
            browser.get(url);

            browser.humanComment("Обращение к всплывающему меню профиля");
            we = browser.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[2]/img"));
            we.click();

            //@todo for what?
            Thread.sleep(5000);

            browser.humanComment("Обращение к элементу `Мой профили`");
            we = browser.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[3]/div[1]/a"));
            if (!we.getText().equals("Мои профили")) {
                telegram.alert("Не удалось найти элемент. " + we.getText());
                throw new Exception("see telegram alerts");
            }
            we.click();

            browser.humanComment("Обращение к элементы заголовка профиля " + profile.getHtmlText() + ". Для проверки порядка профилей");

            final String xpath = "//td/div[contains(text(),'" + profile.getHtmlText() + "')]/..";
            we = browser.waitElement(By.xpath(xpath));
            log.info(xpath);

            log.info(we.getText());
            log.info(we.getTagName());

            if (we.getText().equals(profile.getHtmlText() + "\nТекущий профиль")) {
                return;
            }

            if (!we.getText().equals(profile.getHtmlText())) {
                telegram.alert("Не удалось найти элемент " + profile.getHtmlText() + ". " + we.getText());
                throw new Exception("see telegram alerts");
            }

            browser.humanComment("Обращение к элементы: кнопка переключение на профиль" + profile.getHtmlText());
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

    /**
     * @param phoneNumber
     * @param imShure     если false - выполним код без нажатия кнопки
     * @throws Exception
     */
    private void setPhoneNumber(String phoneNumber, boolean imShure) throws Exception {

        assert phoneNumber != null;

        try {
            String url = "https://taplink.ru/";
            browser.humanComment("Открытие страницы:" + url);
            browser.get(url);

            browser.humanComment("Обращение к блоку WhatsUp.");
            we = browser.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[3]/div[3]/div/div/div/div[1]/div/div/div/div/div/div[7]/div/div/div[2]/div/div/a"));

            if (!we.isDisplayed()) {
                telegram.alert("Блок Whatsup не удалось найти по признаку displayed().", browser.takeSreenshot());
                throw new Exception("see telegram alerts");
            }

            if (!we.getText().equals("Узнать цену в WhatsApp")) {
                telegram.alert("Блок Whatsup не удалось найти по признаку getText().", browser.takeSreenshot());
                throw new Exception("see telegram alerts");
            }

            browser.humanComment("Нажатие на блок WhatsUp");
            we.click();


            browser.humanComment("Обращение к полю телефонного номера");
            we = browser.waitElement(By.xpath("/html/body/div[4]/div[2]/div/section/section[2]/div/div/div/div[2]/div[3]/div/div/input"));

            browser.humanComment("Ввод телефонного номера");
            we.sendKeys("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
            we.sendKeys(phoneNumber);

            //@todo

            browser.humanComment("Обращение к кнопке [Сохранить]");
            we = browser.waitElement(By.xpath("/html/body/div[4]/div[2]/div/footer/div[2]/button"));

            browser.humanComment("Нажатие кнопки [Сохранить]");

            /** Главная кнопка! */
            if (imShure) we.click();


            browser.humanComment("Ожидание Сохранения номера.");
            Thread.sleep(45 * 1000);

        } catch (Exception e) {
            telegram.alert("Установить номер не удалось.", browser.takeSreenshot());
            throw e;
        }
    }

    private void checkThePage(String phoneNumber) throws Exception {

        assert phoneNumber != null;

        try {
            if (!phoneNumber.matches("^\\+7\\d{10}$")) {
                telegram.alert("Номер телефона должен быть в формате +71234567890, передано:'" + phoneNumber + "'");
                throw new Exception("see telegram alerts");
            }

            browser.humanComment("Открытие страницы:" + getPageUrl());
            browser.get(getPageUrl());

            browser.humanComment("Обращение к элементы 'узнать цену в WhatsApp'.");
            we = browser.waitElement(By.xpath("/html/body/div/div[3]/div/div[2]/div[2]/div/main/div/div/div/div/div/div/div[7]/div/div/div/div/a"));

            if (!we.getText().equals("Узнать цену в WhatsApp")) {
                telegram.alert("Не нашелся блок Whatsup по признаку getText(), на странице " + getPageUrl(), browser.takeSreenshot());
                throw new Exception("see telegram alerts");
            }
            if (!we.isDisplayed()) {
                telegram.alert("Не нашелся блок Whatsup по признаку isDisplayed(), на странице " + getPageUrl(), browser.takeSreenshot());
                throw new Exception("see telegram alerts");
            }

            // удаляем знак '+' из номера
            String hrefExpect = "whatsapp://send?phone=" + phoneNumber.substring(1) + "&text=%D0%97%D0%B4%D1%80%D0%B0%D0%B2%D1%81%D1%82%D0%B2%D1%83%D0%B9%D1%82%D0%B5%21%20%D0%AF%20%D1%85%D0%BE%D1%87%D1%83%20%D1%83%D0%B7%D0%BD%D0%B0%D1%82%D1%8C%20%D1%86%D0%B5%D0%BD%D1%83%20%D0%BD%D0%B0%20%D0%BF%D0%BE%D1%80%D1%82%D1%80%D0%B5%D1%82.";
            String hrefFact = we.getAttribute("href");

            if (!hrefFact.equals(hrefExpect)) {
                telegram.alert("Не нашелся блок Whatsup по признаку ссылки в href" +
                        "(ожидалось:'" + hrefExpect + "'," +
                        " фактически:'" + hrefFact + "')" +
                        ", на странице " + getPageUrl(), browser.takeSreenshot());
                throw new Exception("see telegram alerts");
            }

            we.click();

            browser.humanComment("Ожидание нажатие на номер телефона.");
            Thread.sleep(45 * 1000);

            telegram.info("Номер на странице " + getPageUrl() + " соответствует:" + phoneNumber, browser.takeSreenshot());

        } catch (Exception e) {
            e.printStackTrace();
            telegram.alert("Ну удалось проверить страницу ТапЛинк " + getPageUrl() + " , последние действие:" + browser.getHumanComment(), browser.takeSreenshot());
            throw e;
        }
    }

    public void testMultiPage() throws Exception {

        try {
            String phone = getNumber();

            phoneLoggerRepository.save(new PhoneLogger(phone, botContexts.getCurrent()));

        } catch (Exception e) {
            throw e;
        }
    }

    synchronized public String getNumber() throws Exception {

        try {

            browser.humanComment("Открытие страницы:" + getPageUrl());
            browser.get(getPageUrl());

            browser.humanComment("Обращение к элементы 'узнать цену в WhatsApp'.");
            we = browser.waitElement(By.xpath("/html/body/div/div[3]/div/div[2]/div[2]/div/main/div/div/div/div/div/div/div[7]/div/div/div/div/a"));

            if (!we.getText().equals("Узнать цену в WhatsApp")) {
                telegram.alert("Не нашелся блок Whatsup по признаку getText(), на странице " + getPageUrl(), browser.takeSreenshot());
                throw new Exception("see telegram alerts");
            }
            if (!we.isDisplayed()) {
                telegram.alert("Не нашелся блок Whatsup по признаку isDisplayed(), на странице " + getPageUrl(), browser.takeSreenshot());
                throw new Exception("see telegram alerts");
            }

            /* Удаляем знак '+' из номера */
            String hrefFact = we.getAttribute("href");

            hrefFact = hrefFact.replace("whatsapp://send?phone=", "");
            hrefFact = hrefFact.replace("&text=%D0%97%D0%B4%D1%80%D0%B0%D0%B2%D1%81%D1%82%D0%B2%D1%83%D0%B9%D1%82%D0%B5%21%20%D0%AF%20%D1%85%D0%BE%D1%87%D1%83%20%D1%83%D0%B7%D0%BD%D0%B0%D1%82%D1%8C%20%D1%86%D0%B5%D0%BD%D1%83%20%D0%BD%D0%B0%20%D0%BF%D0%BE%D1%80%D1%82%D1%80%D0%B5%D1%82.", "");
            return hrefFact;
        } catch (Exception e) {
            e.printStackTrace();
            telegram.alert("Ну удалось получить номер со страницы ТапЛинк `" + getPageUrl() + "`, последние действие:" + browser.getHumanComment(), browser.takeSreenshot());
            throw e;
        }
    }

    /**
     * Возвращает ссылку на страницу Мультиссылки.
     *
     * @return мультиссылка
     */
    protected String getPageUrl() {
        return "https://" + botContexts.getCurrent().profile.getDomainName() + "/";
    }
}
