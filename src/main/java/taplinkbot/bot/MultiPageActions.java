//FIN
package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import taplinkbot.TapLinkBotException;
import taplinkbot.browser.Browser;
import taplinkbot.components.LangComponent;

@Component
@RequiredArgsConstructor
@Slf4j
public class MultiPageActions {

    private final LangComponent lang;

    private final Browser browser;

    public boolean checkPhoneNumber(Profile profile, String phoneNumber) {

        WebElement we;

        String url = profile.getMultiPageUrl();

        if (!phoneNumber.matches("^\\+7\\d{10}$")) {
            throw new TapLinkBotException(lang.get("actions.phone_number_invalid", phoneNumber));
        }

        browser.setComment(lang.get("actions.get_url", url));
        browser.get(url);

        browser.setComment(lang.get("actions.multipage.whatsup_price"));
        we = browser.waitElement(By.xpath("/html/body/div/div[3]/div/div[2]/div[2]/div/main/div/div/div/div/div/div/div[7]/div/div/div/div/a"));

        if (!we.getText().equals(DataProvider.textWhatsUpPrice)) {
            throw new TapLinkBotException(lang.get("actions.multipage.whatsup_text_failed"));
        }
        if (!we.isDisplayed()) {
            throw new TapLinkBotException(lang.get("actions.multipage.whatsup_displayed_failed"));
        }

        // удаляем знак '+' из номера
        String hrefExpect = "whatsapp://send?phone=" +
                phoneNumber.substring(1) +
                "&text=" +
                DataProvider.textWhatsupHrefPostText;

        String hrefFact = we.getAttribute("href");

        if (!hrefFact.equals(hrefExpect)) {
            throw new TapLinkBotException(lang.get("actions.multipage.whatsup_href_failed", hrefExpect, hrefFact));
        }

        we.click();

        browser.waitElement(By.xpath(DataProvider.xpathApiWhatsUpComChatButton));

        return true;
    }

    public String getNumber(Profile profile) {

        String url = profile.getMultiPageUrl();

        WebElement we;

        browser.setComment(lang.get("actions.get_url", url));
        browser.get(url);

        browser.setComment(lang.get("actions.multipage.whatsup_price"));

        we = browser.waitElement(By.xpath(DataProvider.xpathWhatsUpPrice));

        if (!we.getText().equals(DataProvider.textWhatsUpPrice)) {
            throw new TapLinkBotException(lang.get("actions.multipage.whatsup_text_failed"));
        }

        if (!we.isDisplayed()) {
            throw new TapLinkBotException(lang.get("actions.multipage.whatsup_displayed_failed"));
        }

        /* Удаляем знак '+' из номера */
        String hrefFact = we.getAttribute("href");

        hrefFact = hrefFact.replace("whatsapp://send?phone=", "");
        hrefFact = hrefFact.replace("&text=" + DataProvider.textWhatsupHrefPostText, "");

        return hrefFact;
    }
}
