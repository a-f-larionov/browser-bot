
package browserbot.browser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebDriverFactory {

    @Value("${webdriver.path}")
    private String webDriverPath;

    @Value("${webdriver.--no-sandbox}")
    private String noSandBox;

    @Value("${webdriver.--headless}")
    private String headless;

    @Value("${webdriver.--disable-gpu}")
    private String disableGPU;

    /**
     * Создает веб драйвер
     *
     * @return
     */
    public ChromeDriver buildWebDriver() {
        // Путь к веб драйверу
        System.setProperty("webdriver.chrome.driver", webDriverPath);

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--start-maximized");
        options.addArguments("window-size=1000,2400");

        if (noSandBox.equals("true")) {
            options.addArguments("--no-sandbox");
        }

        if (headless.equals("true")) {
            options.addArguments("--headless");
        }

        // Windows only
        if (disableGPU.equals("true")) {
            options.addArguments("--disable-gpu");
        }

        return new ChromeDriver(options);
    }
}
