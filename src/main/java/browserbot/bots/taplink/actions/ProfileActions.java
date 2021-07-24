
package browserbot.bots.taplink.actions;

import browserbot.BrowserBotException;
import browserbot.bots.taplink.DataProvider;
import browserbot.bots.taplink.Profile;
import browserbot.browser.Browser;
import browserbot.services.LangService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileActions {

    private final LangService lang;

    private final Browser browser;

    public void changeTo(Profile profile) {

        WebElement we;

        String url = DataProvider.urlProfilePages;

        browser.setActionComment(lang.get("actions.get_url", url));
        browser.get(url);

        browser.setActionComment(lang.get("actions.profiles.popup_menu"));
        we = browser.waitElement(By.xpath(DataProvider.xpathProfilesPopUpMenu));
        we.click();

        browser.setActionComment(lang.get("actions.profiles.my_profiles"));
        we = browser.waitElement(By.xpath(DataProvider.xpathProfilesMyProfiles));

        if (!we.getText().equals(DataProvider.textMyProfiles)) {
            throw new BrowserBotException(lang.get("actions.cantfind_element", we.getText()));
        }

        we.click();

        final String xpath = "//td/div[contains(text(),'" + profile.getHtmlText() + "')]/..";

        browser.setActionComment(lang.get("actions.profiles.check_profiles_order", profile.getHtmlText()));
        we = browser.waitElement(By.xpath(xpath));

        // Профиль уже является текущим - ничего не делаем
        if (we.getText().equals(profile.getHtmlText() + "\nТекущий профиль")) {
            return;
        }

        if (!we.getText().equals(profile.getHtmlText())) {
            throw new BrowserBotException(lang.get("actions.cantfind_element", we.getText()));
        }

        browser.setActionComment(lang.get("actions.profiles.button_change_profiles", profile.getHtmlText()));
        final String xpathButton = "//td/div[contains(text(),'" + profile.getHtmlText() + "')]/../../td/button";

        we = browser.waitElement(By.xpath(xpathButton));

        if (!we.getText().equals("Переключиться")) {
            throw new BrowserBotException(lang.get("actions.cantfind_element", we.getText()));
        }
        we.click();

        // Ожидание перазагрузки страниц
        browser.waitElement(By.xpath(DataProvider.xpathAdminWhatsUpBlock));
    }
}
