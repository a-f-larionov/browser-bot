//FIN
package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import taplinkbot.TapLinkBotException;
import taplinkbot.browser.Browser;
import taplinkbot.components.LangComponent;

@Component
@RequiredArgsConstructor
public class PhoneNumberActions {

    private final Browser browser;

    private final LangComponent lang;

    /**
     * Установить новый номер телефона
     * Необходимо авторизоваться прежде чем использовать этот метод
     *
     * @param phoneNumber номер телефона
     * @see AuthActions
     */
    public void setPhoneNumber(String phoneNumber) {

        String url = DataProvider.urlTapLinkIndex;

        browser.setComment(lang.get("actions.get_url"));
        browser.get(url);

        browser.setComment(lang.get("actions.multipage.whatsup_price"));
        WebElement we = browser.waitElement(By.xpath(DataProvider.xpathAdminWhatsUpBlock));

        if (!we.isDisplayed()) {
            throw new TapLinkBotException(lang.get("actions.multipage.whatsup_displayed_failed"));
        }

        if (!we.getText().equals(DataProvider.textWhatsUpPrice)) {
            throw new TapLinkBotException(lang.get("actions.multipage.whatsup_text_failed"));
        }

        browser.setComment(lang.get("actions.block_whatsup_click"));
        we.click();

        browser.setComment(lang.get("actions.block_phone_number_access"));
        we = browser.waitElement(By.xpath(DataProvider.xpathAdminWhatsupPhoneElement));

        browser.setComment("actions.phone_number_enter");
        we.sendKeys("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
        we.sendKeys(phoneNumber);

        browser.setComment(lang.get("actions.admin.save_button_access"));
        we = browser.waitElement(By.xpath(DataProvider.xpathAdminSaveButton));

        browser.setComment(lang.get("actions.admin.save_button_click"));
        we.click();

        browser.setComment(lang.get("actions.admin.wait_number_saved"));

        browser.waitElementDisappear(DataProvider.xpathAdminSaveButton);
    }
}
