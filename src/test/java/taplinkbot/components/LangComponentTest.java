package taplinkbot.components;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.NoSuchMessageException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class LangComponentTest {

    @Autowired
    private LangComponent lang;

    @Test
    void get() {

        String expectText = "This message for test LangComponentTest";

        assertThat(
                lang.get("test.test")
        ).isEqualTo(expectText);
    }

    @Test
    void getNotFoundMessage() {

        assertThatThrownBy(
                () -> lang.get("message.not.found.in.messages.files")
        ).isInstanceOf(NoSuchMessageException.class);
    }
}