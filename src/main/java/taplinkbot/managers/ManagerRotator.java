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
        System.out.println("++++++++getNextManager");
        incrementIndex();

        return getCurrentManager();
    }

    public Manager getPrevManager() {
        System.out.println("++++++++getPrevManager");

        decrementIndex();

        return getCurrentManager();
    }

    private void incrementIndex() {
        System.out.println("++++++++incrementIndex");
        int lastIndex = stateService.getManagerLastIndex();
        System.out.println("++++++++ " + lastIndex);
        lastIndex++;

        if (lastIndex >= managers.length) lastIndex = 0;


        System.out.println("++++++++ " + lastIndex);
        stateService.setManagerLastIndex(lastIndex);
    }

    private void decrementIndex() {
        System.out.println("++++++++decrementIndex");
        int lastIndex = stateService.getManagerLastIndex();
        System.out.println("++++++++ " + lastIndex);
        lastIndex--;

        if (lastIndex == -1) lastIndex = 0;


        System.out.println("++++++++ " + lastIndex);
        stateService.setManagerLastIndex(lastIndex);
    }

    public Manager getCurrentManager() {
        System.out.println("++++++++getCurrentManager");
        int lastIndex = stateService.getManagerLastIndex();
        System.out.println("++++++++ " + lastIndex);
        return managers[lastIndex];
    }

    public void setIndex(int i) {
        stateService.setManagerLastIndex(i);
    }

    public Manager[] getList() {

        return managers;
    }
}
