package taplinkbot.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import taplinkbot.service.StateService;

@Component
public class ManagerRotator {

    private Manager[] managers = {
            Manager.Manager9206,
            Manager.Manager3990,
            //Manager.Manager9434
    };

    @Autowired
    private StateService stateService;

    public Manager getNextManager() {

        int lastIndex = stateService.getManagerLastIndex();

        lastIndex++;

        if (lastIndex >= managers.length) {
            lastIndex = 0;
        }

        stateService.setManagerLastIndex(lastIndex);

        return managers[lastIndex];
    }

    public void setIndex(int i) {
        stateService.setManagerLastIndex(i);
    }

    public Manager[] getList() {

        return managers;
    }
}
