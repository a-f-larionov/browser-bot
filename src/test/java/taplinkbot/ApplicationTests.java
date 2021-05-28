package taplinkbot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import taplinkbot.controllers.IndexController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private IndexController indexController;

    @Test
    void contextLoads() {

    }

    @Test
    void indexControllerInstantiates() {
        assertThat(indexController).isNotNull();
    }

    @Test
    void testTimeZoneSetted() {
//@Todo

    }
}
