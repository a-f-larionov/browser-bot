package browserbot.telegram.commands;

import browserbot.browser.Browser;
import browserbot.telegram.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@CommandClass(name = "/instagram_liker")
public class InstagramLiker extends Command {

    private final Browser browser;

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

        log.info(browser.takeScreenshot());
        log.info("1");
        // wait for login field
        browser.waitElement(By.xpath("//*[@id='loginForm']/div/div[1]/div/label/input"));
        log.info("1");
        // wait for password field
        browser.waitElement(By.xpath("//*[@id='loginForm']/div/div[2]/div/label/input"));
        log.info("1");

        return MessageBuilder.buildResult(browser.takeScreenshot());
    }
}
