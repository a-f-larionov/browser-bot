package taplinkbot.managers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taplinkbot.entities.Manager;
import taplinkbot.repositories.ManagerRepository;
import taplinkbot.service.Settings;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ManagerRotator {

    private Manager[] managers;

    private final Settings settings;

    private final ManagerRepository managerRepository;

    @PostConstruct
    private void init() {
        List<Manager> all = managerRepository.findAll();

        managers = new Manager[all.size()];

        managers = all.toArray(managers);

        for (int i = 0; i < managers.length; i++) {
            managers[i].setIndex(i);
        }
    }

    public Manager getNextManager() {

        incrementIndex(settings.getManagerIndex());

        return getCurrentManager();
    }

    private void incrementIndex(int startIndex) {

        int index = settings.getManagerIndex();

        index++;

        if (index >= managers.length) index = 0;

        settings.setManagerIndex(index);

        if (!getCurrentManager().isWorking()) {
            if (startIndex != getCurrentManager().getIndex()) {
                incrementIndex(startIndex);
            }
        }
    }

    public Manager getCurrentManager() {

        int index = settings.getManagerIndex();

        return managers[index];
    }

    public void setIndex(int i) {
        settings.setManagerIndex(i);
    }

    public Manager[] getList() {

        return managers;
    }

    public void isWorkingChange(int managerId) {

        for (Manager manager : managers) {
            if (manager.getId() == managerId) {
                manager.setWorking(!manager.isWorking());
                managerRepository.save(manager);
            }
        }

    }
}
