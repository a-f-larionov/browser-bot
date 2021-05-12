package taplinkbot.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Actions;
import taplinkbot.bot.Profile;
import taplinkbot.bot.Profiles;
import taplinkbot.browser.Browser;
import taplinkbot.entities.Manager;
import taplinkbot.managers.ManagerRotator;
import taplinkbot.telegram.TelegramBot;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    private final TelegramBot telegram;

    private final Profiles profiles;

    private final Trigger trigger;

    private final ManagerRotator rotator;

    private final Actions actions;

    private final Browser browser;

    @Scheduled(cron = "0 * * * * 1-7")
    public void intervaled() {

        try {
            onIdleTestBugErrConnectionClosed();

            onIdleManagerChange(Profile.Canvas);

            onIdleManagerChange(Profile.LadyArt);

            onIdlePinger(Profile.Canvas);

            onIdlePinger(Profile.LadyArt);

        } catch (Exception e) {
            //@Todo create annotation @BotExceptionHandler
            e.printStackTrace();
            telegram.alert(e.getMessage());
        }
    }

    private void onIdleTestBugErrConnectionClosed() {

        browser.testBugErrConnectionClosed();
    }

    private void onIdlePinger(Profile profile) throws Exception {

        profiles.set(profile);

        multiPageControl(profile);

        profiles.clear();
    }

    private void onIdleManagerChange(Profile profile) throws Exception {

        profiles.set(profile);

        if (!trigger.isItTimeToChange(profile)) {
            return;
        }

        Manager manager = rotator.getNextManager(profile);

        log.info("Установка менеджера(" + profile.name + "):" + manager.getDescription());

        setNewManager(manager, profile);

        trigger.updateLastTime(profile);

        profiles.clear();
    }

    private void setNewManager(Manager manager, Profile profile) throws Exception {
        profiles.set(profile);
        telegram.info("Смена номера: " + profile.name + " " + manager.getDescription());

        actions.setPhoneNumber(manager.getPhone(), profile);
    }

    private void multiPageControl(Profile profile) throws Exception {
        profiles.set(profile);

        actions.multiPageControl(profile);
    }
}

