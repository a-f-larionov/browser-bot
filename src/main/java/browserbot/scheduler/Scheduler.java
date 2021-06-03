package browserbot.scheduler;

import browserbot.bots.taplink.Profile;
import browserbot.telegram.CommandExecutor;
import browserbot.telegram.commands.CheckAndExecuteRotator;
import browserbot.telegram.commands.ControlMultiPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    private final CommandExecutor commandExecutor;

    @Scheduled(cron = "0 * * * * 1-7")
    public void tick() {

        log.info("tick");

        onIdleManagerChange(Profile.Canvas);

        onIdleManagerChange(Profile.LadyArt);

        onIdlePinger(Profile.Canvas);

        onIdlePinger(Profile.LadyArt);
    }

    private void onIdleManagerChange(Profile profile) {

        commandExecutor.execute(CheckAndExecuteRotator.class, profile);
    }

    private void onIdlePinger(Profile profile) {

        commandExecutor.execute(ControlMultiPage.class, profile);
    }
}

