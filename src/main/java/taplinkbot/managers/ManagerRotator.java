package taplinkbot.managers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taplinkbot.service.StateService;

@Component
@RequiredArgsConstructor
@Slf4j
public class ManagerRotator {

    private Manager[] managers = {
            Manager.Manager9206,
            Manager.Manager3990,
            Manager.Manager9434,
            Manager.Manager0307,
    };

    private final StateService stateService;

    public Manager getNextManager() {

        incrementIndex();

        return getCurrentManager();
    }

    public Manager getPrevManager() {

        decrementIndex();

        return getCurrentManager();
    }

    private void incrementIndex() {
        log.info("++++++++incrementIndex");
        int lastIndex = stateService.getManagerLastIndex();
        log.info("++++++++ " + lastIndex);
        lastIndex++;

        if (lastIndex >= managers.length) lastIndex = 0;

        log.info("++++++++ " + lastIndex);
        stateService.setManagerLastIndex(lastIndex);
    }

    private void decrementIndex() {
        log.info("++++++++decrementIndex");
        int lastIndex = stateService.getManagerLastIndex();
        log.info("++++++++ " + lastIndex);
        lastIndex--;

        if (lastIndex == -1) lastIndex = 0;


        log.info("++++++++ " + lastIndex);
        stateService.setManagerLastIndex(lastIndex);
    }

    public Manager getCurrentManager() {
        log.info("++++++++getCurrentManager");
        int lastIndex = stateService.getManagerLastIndex();
        log.info("++++++++ " + lastIndex);
        return managers[lastIndex];
    }

    public void setIndex(int i) {
        stateService.setManagerLastIndex(i);
    }

    public Manager[] getList() {

        return managers;
    }
}
