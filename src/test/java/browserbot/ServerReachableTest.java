package browserbot;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
public class ServerReachableTest {

    @LocalServerPort
    private int port;

    @Autowired
    private final TestRestTemplate testRestTemplate;

    @Test
    void testHttpRequest() {

        String content = testRestTemplate.getForObject(
                "http://localhost:" + port + "/",
                String.class
        );

        assertThat(content).contains("TapLinkBot Control GUI");
    }
}
