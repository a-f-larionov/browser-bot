package taplinkbot.browser;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BrowserTest {

    @Autowired
    private Environment env;

    @Test
    public void testConfig() {

        assertThat(
                env.getProperty("webdriver.elementTimeoutSeconds")
        ).isNotEmpty();
    }

    //@todo
    public void testWaitElementTimeout() {
        /**
         * Mock
         * call Browswer.waitElement()
         * expect waitElemnt( default time out);
         */
    }
}