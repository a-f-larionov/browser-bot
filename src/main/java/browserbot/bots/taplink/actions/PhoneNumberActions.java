
package browserbot.bots.taplink.actions;

import browserbot.bots.taplink.DataProvider;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import browserbot.BrowserBotException;
import browserbot.browser.Browser;
import browserbot.services.LangService;

@Component
@RequiredArgsConstructor
public class PhoneNumberActions {

    private final Browser browser;

    private final LangService lang;

    /**
     * Установить новый номер телефона
     * Необходимо авторизоваться прежде чем использовать этот метод
     *
     * @param phoneNumber номер телефона
     * @see AuthActions
     */
    public void setPhoneNumber(String phoneNumber) {

        String url = DataProvider.urlTapLinkIndex;

        browser.setActionComment(lang.get("actions.get_url", url));
        browser.get(url);

        browser.setActionComment(lang.get("actions.multipage.whatsup_price"));
        WebElement we = browser.waitElement(By.xpath(DataProvider.xpathAdminWhatsUpBlock));

        if (!we.isDisplayed()) {
            throw new BrowserBotException(lang.get("actions.multipage.whatsup_displayed_failed"));
        }

        if (!we.getText().equals(DataProvider.textWhatsUpPrice)) {
            throw new BrowserBotException(lang.get("actions.multipage.whatsup_text_failed"));
        }

        browser.setActionComment(lang.get("actions.block_whatsup_click"));
        we.click();

        browser.setActionComment(lang.get("actions.block_phone_number_access"));
        we = browser.waitElement(By.xpath(DataProvider.xpathAdminWhatsupPhoneElement));

        browser.setActionComment("actions.phone_number_enter");
        we.sendKeys("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
        we.sendKeys(phoneNumber);

        browser.setActionComment(lang.get("actions.admin.save_button_access"));
        we = browser.waitElement(By.xpath(DataProvider.xpathAdminSaveButton));

        browser.setActionComment(lang.get("actions.admin.save_button_click"));
        we.click();

        browser.setActionComment(lang.get("actions.admin.wait_number_saved"));
        browser.waitElementDisappear(By.xpath(DataProvider.xpathAdminSaveButton));
    }
}
