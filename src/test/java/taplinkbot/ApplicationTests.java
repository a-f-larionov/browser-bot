package taplinkbot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import taplinkbot.controllers.AdminController;
import taplinkbot.controllers.ManagerController;
import taplinkbot.controllers.UserController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private AdminController adminController;

    @Autowired
    private ManagerController managerController;

    @Autowired
    private UserController userController;

    @Test
    void contextLoads() {

    }

    @Test
    void indexControllerInstantiates() {
        assertThat(adminController).isNotNull();
    }

    @Test
    void managerControllerInstantiates() {
        assertThat(managerController).isNotNull();
    }

    @Test
    void userControllerInstantiates() {
        assertThat(userController).isNotNull();
    }

    @Test
    void testTimeZoneSetted() {
//@Todo

    }
}
