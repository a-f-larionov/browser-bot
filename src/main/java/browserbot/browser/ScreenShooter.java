//FIN
package browserbot.browser;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class ScreenShooter {

    @Value("${webdriver.screenshot.path}")
    private String filePath;

    @Value("${webdriver.screenshot.hostname}")
    private String hostname;

    /**
     * Возвращает url скриншота браузера.
     *
     * @param driver драйвер браузера
     * @return url скришота
     */
    @SneakyThrows
    public String takeScreenshot(RemoteWebDriver driver) {

        String url, fileName;

        fileName = "botscreen_" + System.currentTimeMillis() + ".png";

        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = new File(filePath + fileName);

        FileUtils.copyFile(source, dest);

        url = "http://" + hostname + "/" + fileName;

        return url;
    }
}
