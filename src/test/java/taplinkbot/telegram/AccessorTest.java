package taplinkbot.telegram;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


public class AccessorTest {

    @Autowired
    public Accessor accessor;

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

        Request msg = new Request();

        msg.initiatorChatId = String.valueOf(-1001232151616L);

        assertThat(accessor.check(msg)).isTrue();
    }

    @Test
    void checkTestGroup() {

        Request msg = new Request();

        msg.initiatorChatId = String.valueOf(-439603549);

        assert accessor != null;

        assertThat(accessor.check(msg)).isTrue();
    }


    @Test
    void negativeTest1() {

        String[] chatIds = {"123", "0", "-12321", "123213", "lkj", "", null};

        for (String chatId : chatIds) {
            Request msg = new Request();
            msg.initiatorChatId = chatId;
            assertThat(accessor.check(msg)).isFalse();
        }
    }
}