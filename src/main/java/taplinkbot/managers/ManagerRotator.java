package taplinkbot.managers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Profile;
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

    public Manager getNextManager(Profile profile) {

        incrementIndex(profile, settings.getManagerIndex(profile));

        return getCurrentManager(profile);
    }

    private void incrementIndex(Profile profile, int startIndex) {

        int index = settings.getManagerIndex(profile);

        index++;

        if (index >= managers.length) index = 0;

        settings.setManagerIndex(profile, index);

        if (!getCurrentManager(profile).isWorking()) {
            if (startIndex != getCurrentManager(profile).getIndex()) {
                incrementIndex(profile, startIndex);
            }
        }
    }

    public Manager getCurrentManager(Profile profile) {

        int index = settings.getManagerIndex(profile);

        return managers[index];
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
