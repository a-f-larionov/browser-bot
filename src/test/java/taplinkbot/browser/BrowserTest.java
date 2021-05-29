package taplinkbot.browser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

import javax.xml.xpath.XPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    //@Todo
    public void test() {

        browser.waitElement(By.xpath());
    }
}