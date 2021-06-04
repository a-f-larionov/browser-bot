//FIN
package browserbot.telegram.commands;

import browserbot.browser.Browser;
import browserbot.telegram.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@CommandClass(name = "/instagram_liker")
public class InstagramLiker extends Command {

    private final Browser browser;

    private final Environment env;

    @Override
    public String getDescription() {
        return "Команда хорошая";
    }

    @Override
    @SneakyThrows
    public Reponse run(Request msg) {
//@Todo
        browser.get("https://www.instagram.com");

        Thread.sleep(5000);
        String loginField = "//*[@id='loginForm']/div/div[1]/div/label/input";
        String passwordField = "//*[@id='loginForm']/div/div[2]/div/label/input";
        String loginButton = "//*[@id=\"loginForm\"]/div/div[3]/button";

        log.info(browser.takeScreenshot());
        // login field
        browser.waitElement(By.xpath(loginField)).sendKeys(env.getProperty("instagram.login"));
        //  password field
        browser.waitElement(By.xpath(passwordField)).sendKeys(env.getProperty("instagram.password"));
        // log in button
        browser.waitElement(By.xpath(loginButton)).click();

        // если появиласяь кнопка сохранить данные авторизации
        By bySaveAuthData = By.xpath("//*[@id=\"react-root\"]/section/main/div/div/div/section/div/button");
        if (browser.isElementPresent(bySaveAuthData)) {
            browser.waitElement(bySaveAuthData).click();
        }

        // если появилась кнопка не сейчас
        By byNotNowButton = By.xpath("/html/body/div[4]/div/div/div/div[3]/button[2]");
        if (false && browser.isElementPresent(bySaveAuthData)) {
            browser.waitElement(bySaveAuthData).click();
        }


        Thread.sleep(5000);
//wait page loading
        browser.waitElement(By.xpath("//*[@id=\"react-root\"]/section/nav/div[2]/div/div/div[2]/div[1]/div"));

        String JS;

        JS = "\n" +
                "document.querySelectorAll(\"  button > div > span > svg\").forEach(function(e){\n" +
                "console.log(e.getAttribute(\"aria-label\"));\n" +
                "var ariaLabel = e.getAttribute('aria-label');\n" +
                "\n" +
                "if(ariaLabel==\"Нравиться\"){\n" +
                "     e.parentNode.click();\n" +
                "}\n" +
                "});"
        ;
        log.info(browser.takeScreenshot());
        browser.executeScript(JS);
        Thread.sleep(5000);
        log.info(browser.takeScreenshot());

        return MessageBuilder.buildResult(browser.takeScreenshot());
    }
}
