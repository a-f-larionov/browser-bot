//FIN
package browserbot.bots.taplink.actions;

import browserbot.bots.taplink.DataProvider;
import browserbot.bots.taplink.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import browserbot.BrowserBotException;
import browserbot.browser.Browser;
import browserbot.services.LangService;

@Component
@RequiredArgsConstructor
@Slf4j
public class MultiPageActions {

    private final LangService lang;

    private final Browser browser;

    public boolean checkPhoneNumber(Profile profile, String phoneNumber) {

        WebElement we;

        String url = profile.getMultiPageUrl();

        if (!phoneNumber.matches("^\\+7\\d{10}$")) {
            throw new BrowserBotException(lang.get("actions.phone_number_invalid", phoneNumber));
        }

        browser.setActionComment(lang.get("actions.get_url", url));
        browser.get(url);

        browser.setActionComment(lang.get("actions.multipage.whatsup_price"));
        we = browser.waitElement(By.xpath("/html/body/div/div[3]/div/div[2]/div[2]/div/main/div/div/div/div/div/div/div[7]/div/div/div/div/a"));

        if (!we.getText().equals(DataProvider.textWhatsUpPrice)) {
            throw new BrowserBotException(lang.get("actions.multipage.whatsup_text_failed"));
        }
        if (!we.isDisplayed()) {
            throw new BrowserBotException(lang.get("actions.multipage.whatsup_displayed_failed"));
        }

        // удаляем знак '+' из номера
        String hrefExpect = "whatsapp://send?phone=" +
                phoneNumber.substring(1) +
                "&text=" +
                DataProvider.textWhatsupHrefPostText;

        String hrefFact = we.getAttribute("href");

        if (!hrefFact.equals(hrefExpect)) {
            throw new BrowserBotException(lang.get("actions.multipage.whatsup_href_failed", hrefExpect, hrefFact));
        }

        log.info(browser.takeScreenshot());
        we.click();

        log.info(browser.takeScreenshot());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info(browser.takeScreenshot());

        browser.waitElement(By.xpath(DataProvider.xpathWhatsupApiPageMessage));

        log.info(browser.takeScreenshot());
        return true;
    }

    public String getNumber(Profile profile) {

        String url = profile.getMultiPageUrl();

        WebElement we;

        browser.setActionComment(lang.get("actions.get_url", url));
        browser.get(url);

        browser.setActionComment(lang.get("actions.multipage.whatsup_price"));

        we = browser.waitElement(By.xpath(DataProvider.xpathWhatsUpPrice));

        if (!we.getText().equals(DataProvider.textWhatsUpPrice)) {
            throw new BrowserBotException(lang.get("actions.multipage.whatsup_text_failed"));
        }

        if (!we.isDisplayed()) {
            throw new BrowserBotException(lang.get("actions.multipage.whatsup_displayed_failed"));
        }

        /* Удаляем знак '+' из номера */
        String hrefFact = we.getAttribute("href");

        hrefFact = hrefFact.replace("whatsapp://send?phone=", "");
        hrefFact = hrefFact.replace("&text=" + DataProvider.textWhatsupHrefPostText, "");

        return hrefFact;
    }
}
