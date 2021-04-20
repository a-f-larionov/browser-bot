package taplinkbot.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import taplinkbot.entities.PhoneLogger;
import taplinkbot.repositories.PhoneLoggerRepository;
import taplinkbot.service.StateService;
import taplinkbot.telegram.TelegramBot;


@Component
@RequiredArgsConstructor
@Slf4j
abstract public class CommonActions {

    protected final Semaphore semaphore;

    protected final TelegramBot telegram;

    protected final DriverWrapper wrapper;

    protected final StateService stateService;

    protected final PhoneLoggerRepository phoneLoggerRepository;

    protected final Environment env;

    private WebElement we;

    abstract protected Profile getProfile();

    abstract protected String getPageUrl();

    public void authAndUpdatePhone(String phoneNumber, boolean stepsInfo, boolean imShure) {

        assert phoneNumber != null;

        if (!phoneNumber.matches("^\\+7\\d{10}$")) {
            telegram.info("Номер телефона должен быть в формате +71234567890, передано:'" + phoneNumber + "'");
            return;
        }

        try {
            if (semaphore.lock()) {

                if (stepsInfo) telegram.info("Авторизация");
                authorize();
                if (stepsInfo) telegram.info("Авторизация успешна", wrapper.takeSreenshot());

                if (stepsInfo) telegram.info("Смена профиля");
                changeProfile(getProfile());

                if (stepsInfo) telegram.info("Установка номера" + phoneNumber);
                setPhoneNumber(phoneNumber, imShure);

                if (stepsInfo) telegram.info("Проверка страницы TapLink" + stateService.getBotContext().name);

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

    @Getter
    enum Profile {

        Canvas("canvas.ru.com"),
        LadyArt("lady-art.art");

        private String htmlText;

        Profile(String htmlText) {
            this.htmlText = htmlText;
        }

    }

    private void changeProfile(Profile profile) throws Exception {

        assert profile != null;

        try {

            String url = "https://taplink.ru/profile/2988200/pages/";
            wrapper.humanComment("Открытие страницы:" + "https://taplink.ru/profile/2988200/pages/");
            wrapper.get(url);

            wrapper.humanComment("Обращение к всплывающему меню профиля");
            we = wrapper.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[2]/img"));
            we.click();

            //@todo for what?
            Thread.sleep(5000);

            wrapper.humanComment("Обращение к элементу `Мой профили`");
            we = wrapper.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[3]/div[1]/a"));
            if (!we.getText().equals("Мои профили")) {
                telegram.alert("Не удалось найти элемент. " + we.getText());
                throw new Exception("see telegram alerts");
            }
            we.click();

            wrapper.humanComment("Обращение к элементы заголовка профиля " + profile.getHtmlText() + ". Для проверки порядка профилей");

            final String xpath = "//td/div[contains(text(),'" + profile.getHtmlText() + "')]/..";
            we = wrapper.waitElement(By.xpath(xpath));
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

            wrapper.humanComment("Обращение к элементы: кнопка переключение на профиль" + profile.getHtmlText());
            final String xpathButton = "//td/div[contains(text(),'" + profile.getHtmlText() + "')]/../../td/button";
            we = wrapper.waitElement(By.xpath(xpathButton));
            if (!we.getText().equals("Переключиться")) {
                telegram.alert("Не удалось найти элемент `переключиться`. " + we.getText());
                throw new Exception("see telegram alerts");
            }
            we.click();

            //@todo for what?
            Thread.sleep(5000);

        } catch (Exception e) {

            telegram.alert("Смена профиля не удалась.", wrapper.takeSreenshot());
            throw e;
        }
    }

    private void authorize() {

        try {

            //@todo check if autorized - do not repeat it! or reset

            //@todo this account priznak
            if (checkIsAuthorized()) return;

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

        assert we != null;

        wrapper.humanComment("Ввод логина");
        we.sendKeys(getLogin());
    }

    private void enterPassword() {
        wrapper.humanComment("Обращение к поле ввода пароля");
        we = wrapper.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/div[2]/div[2]/input"));

        assert we != null;

        wrapper.humanComment("Ввод пароля");
        we.sendKeys(getPassword());
    }

    private String getLogin() {
        return getProp("username");
    }

    private String getPassword() {
        return getProp("password");
    }

    private String getProp(String name) {
        String propName = "taplink." +
                stateService
                        .getBotContext()
                        .name
                + "." + name;
        return env.getProperty(propName);
    }

    private void authSubmit() {
        wrapper.humanComment("Обращение к кнопки авторизации");
        we = wrapper.findElement(By.xpath("/html/body/div[1]/div[4]/section/div[2]/div/div[2]/form/button"));

        assert we != null;

        wrapper.humanComment("Нажатие кнопки авторизации");
        we.click();

        wrapper.humanComment("Проверка начилия иконки профиля(проверка авторизации)");
        wrapper.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[2]/header/div/div[1]/div[2]/div/div/div[2]/img"));

        checkIsAuthorized();

        try {
            //@todo for what?
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private boolean checkIsAuthorized() {
        //@todo cabinet?

        String url = "https://taplink.ru/profile/2988200/account/settings/";
        wrapper.humanComment("Открытие страницы:" + url);
        wrapper.get(url);

        System.out.println(wrapper.takeSreenshot());

        try {
            wrapper.skipOneAlert();
            we = wrapper.waitElement(By.xpath("/html/body/div[1]/div[4]/div/div[3]/div/div[1]/div[2]/div/div/div/div[1]/div/p/div/div/div/div/input"), 5);
        } catch (Exception e) {
            return false;
        }

        String value = we.getAttribute("value");
        log.info("авторизованно:" + value);

        return getLogin().equals(value);
    }

    /**
     * @param phoneNumber
     * @param imShure     если false - выполним код без нажатия кнопки
     * @throws Exception
     */
    public void setPhoneNumber(String phoneNumber, boolean imShure) throws Exception {

        assert phoneNumber != null;

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

            /** Главная кнопка! */
            if (imShure) we.click();


            wrapper.humanComment("Ожидание Сохранения номера.");
            Thread.sleep(45 * 1000);

        } catch (Exception e) {
            telegram.alert("Установить номер не удалось.", wrapper.takeSreenshot());
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

            wrapper.humanComment("Открытие страницы:" + getPageUrl());
            wrapper.get(getPageUrl());

            wrapper.humanComment("Обращение к элементы 'узнать цену в WhatsApp'.");
            we = wrapper.waitElement(By.xpath("/html/body/div/div[3]/div/div[2]/div[2]/div/main/div/div/div/div/div/div/div[7]/div/div/div/div/a"));

            if (!we.getText().equals("Узнать цену в WhatsApp")) {
                telegram.alert("Не нашелся блок Whatsup по признаку getText(), на странице " + getPageUrl(), wrapper.takeSreenshot());
                throw new Exception("see telegram alerts");
            }
            if (!we.isDisplayed()) {
                telegram.alert("Не нашелся блок Whatsup по признаку isDisplayed(), на странице " + getPageUrl(), wrapper.takeSreenshot());
                throw new Exception("see telegram alerts");
            }

            // удаляем знак '+' из номера
            String hrefExpect = "whatsapp://send?phone=" + phoneNumber.substring(1) + "&text=%D0%97%D0%B4%D1%80%D0%B0%D0%B2%D1%81%D1%82%D0%B2%D1%83%D0%B9%D1%82%D0%B5%21%20%D0%AF%20%D1%85%D0%BE%D1%87%D1%83%20%D1%83%D0%B7%D0%BD%D0%B0%D1%82%D1%8C%20%D1%86%D0%B5%D0%BD%D1%83%20%D0%BD%D0%B0%20%D0%BF%D0%BE%D1%80%D1%82%D1%80%D0%B5%D1%82.";
            String hrefFact = we.getAttribute("href");

            if (!hrefFact.equals(hrefExpect)) {
                telegram.alert("Не нашелся блок Whatsup по признаку ссылки в href" +
                        "(ожидалось:'" + hrefExpect + "'," +
                        " фактически:'" + hrefFact + "')" +
                        ", на странице " + getPageUrl(), wrapper.takeSreenshot());
                throw new Exception("see telegram alerts");
            }

            we.click();

            wrapper.humanComment("Ожидание нажатие на номер телефона.");
            Thread.sleep(45 * 1000);

            telegram.info("Номер на странице " + getPageUrl() + " соответствует:" + phoneNumber, wrapper.takeSreenshot());

        } catch (Exception e) {
            e.printStackTrace();
            telegram.alert("Ну удалось проверить страницу ТапЛинк " + getPageUrl() + " , последние действие:" + wrapper.getHumanComment(), wrapper.takeSreenshot());
            throw e;
        }
    }

    public void checkPage() throws Exception {

        try {
            String phone = getNumber();

            phoneLoggerRepository.save(new PhoneLogger(phone, stateService.getBotContext()));

        } catch (Exception e) {
            throw e;
        }
    }

    public String getNumber() throws Exception {

        try {
            if (semaphore.lock()) {
                wrapper.humanComment("Открытие страницы:" + getPageUrl());
                wrapper.get(getPageUrl());

                wrapper.humanComment("Обращение к элементы 'узнать цену в WhatsApp'.");
                we = wrapper.waitElement(By.xpath("/html/body/div/div[3]/div/div[2]/div[2]/div/main/div/div/div/div/div/div/div[7]/div/div/div/div/a"));

                if (!we.getText().equals("Узнать цену в WhatsApp")) {
                    telegram.alert("Не нашелся блок Whatsup по признаку getText(), на странице " + getPageUrl(), wrapper.takeSreenshot());
                    throw new Exception("see telegram alerts");
                }
                if (!we.isDisplayed()) {
                    telegram.alert("Не нашелся блок Whatsup по признаку isDisplayed(), на странице " + getPageUrl(), wrapper.takeSreenshot());
                    throw new Exception("see telegram alerts");
                }

                /** удаляем знак '+' из номера */
                String hrefFact = we.getAttribute("href");

                hrefFact = hrefFact.replace("whatsapp://send?phone=", "");
                hrefFact = hrefFact.replace("&text=%D0%97%D0%B4%D1%80%D0%B0%D0%B2%D1%81%D1%82%D0%B2%D1%83%D0%B9%D1%82%D0%B5%21%20%D0%AF%20%D1%85%D0%BE%D1%87%D1%83%20%D1%83%D0%B7%D0%BD%D0%B0%D1%82%D1%8C%20%D1%86%D0%B5%D0%BD%D1%83%20%D0%BD%D0%B0%20%D0%BF%D0%BE%D1%80%D1%82%D1%80%D0%B5%D1%82.", "");
                return hrefFact;
            } else {
                return "Бот заблокирован, попробуйте позже.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            telegram.alert("Ну удалось получить номер со страницы ТапЛинк `" + getPageUrl() + "` ,последние действие:" + wrapper.getHumanComment(), wrapper.takeSreenshot());
            throw e;
        } finally {
            semaphore.unlock();
        }
    }
}
