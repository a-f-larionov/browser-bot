package taplinkbot.telegram;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class AccessorTest {

    @Autowired
    private Accessor accessor;

    /**
     * Ларионов(разработчик): 149798103
     * Группа c Дмитрием Фоминым(заказчик), TapLinkBot: -1001232151616
     * Группа тестовая: -439603549
     */
    @Test
    void checkLarionov() {

        Request msg = new Request();

        msg.initiatorChatId = String.valueOf(149798103);

        assertThat(accessor.check(msg)).isTrue();
    }

    @Test
    void checkAdminGroup() {

        Request req = new Request();

        req.initiatorChatId = String.valueOf(-1001232151616L);

        assertThat(accessor.check(req)).isTrue();
    }

    @Test
    void checkTestGroup() {

        Request req = new Request();

        req.initiatorChatId = String.valueOf(-439603549);

        assert accessor != null;

        assertThat(accessor.check(req)).isTrue();
    }

    @Test
    void negativeTest1() {

        String[] chatIds = {"123", "0", "-12321", "123213", "lkj", ""};

        for (String chatId : chatIds) {
            Request req = new Request();
            req.initiatorChatId = chatId;

            assertThat(
                    accessor.check(req)
            ).isFalse();
        }
    }

    @Test
    void nullPointerCheck() {

        Request req = new Request();
        req.initiatorChatId = null;

        assertThatThrownBy(() -> accessor.check(req))
                .isInstanceOf(NullPointerException.class);
    }
}