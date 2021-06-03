package browserbot.components;

import browserbot.services.LangService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.NoSuchMessageException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class LangServiceTest {

    @Autowired
    private LangService lang;

    @Test
    void get() {

        String expectText = "This message for test LangServiceTest";

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