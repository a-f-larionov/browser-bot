
package browserbot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Компонент ответчает за аккуант на таплинке
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TapLinkAccount {

    private final Environment env;

    public String getLogin() {
        return env.getProperty("taplink.account.username");
    }

    public String getPassword() {
        return env.getProperty("taplink.account.password");
    }
}
