package taplinkbot.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Profile;
import taplinkbot.telegram.CommandExecutor;
import taplinkbot.telegram.commands.CheckAndExecuteRotator;
import taplinkbot.telegram.commands.ControlMultiPage;
import taplinkbot.telegram.commands.FixBugErrConnectionClosed;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    private final CommandExecutor commandExecutor;

    @Scheduled(cron = "0 * * * * 1-7")
    public void tick() {

        log.info("tick");

        onIdleTestBugErrConnectionClosed();

        onIdleManagerChange(Profile.Canvas);

        onIdleManagerChange(Profile.LadyArt);

        onIdleManagerChange(Profile.Test1);

        onIdlePinger(Profile.Canvas);

        onIdlePinger(Profile.LadyArt);
    }

    private void onIdleTestBugErrConnectionClosed() {

        commandExecutor.execute(FixBugErrConnectionClosed.class);
    }

    private void onIdleManagerChange(Profile profile) {

        commandExecutor.execute(CheckAndExecuteRotator.class, profile);
    }

    private void onIdlePinger(Profile profile) {

        commandExecutor.execute(ControlMultiPage.class, profile);
    }
}

