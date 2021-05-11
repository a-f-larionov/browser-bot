package taplinkbot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaplinkAcсount {

    private final Environment env;

    public String getLogin() {
        return env.getProperty("taplink.account.username");
    }

    public String getPassword() {
        return env.getProperty("taplink.account.password");
    }
}
