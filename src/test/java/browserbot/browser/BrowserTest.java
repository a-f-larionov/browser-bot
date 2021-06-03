package browserbot.browser;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BrowserTest {

    @Autowired
    private Environment env;

    @Autowired
    Browser browser;

    @Test
    public void testConfig() {
        assertThat(
                env.getProperty("webdriver.waitElementSeconds")
        ).isNotEmpty();
    }

    @Test
    public void test() {

        By by = By.xpath("/");

        int waitElementSeconds = Integer.parseInt(
                Objects.requireNonNull(
                        env.getProperty("webdriver.waitElementSeconds")
                )
        );

        Browser spy = Mockito.spy(browser);

        Mockito.doReturn(null)
                .when(spy)
                .waitElement(
                        ArgumentMatchers.any(),
                        ArgumentMatchers.anyInt());

        spy.waitElement(by);

        Mockito.verify(spy).waitElement(by, waitElementSeconds);
    }
}