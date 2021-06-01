
package taplinkbot.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Profile;
import taplinkbot.entities.Manager;
import taplinkbot.repositories.ManagerRepository;

import javax.annotation.PostConstruct;
import java.util.List;

//@Todo one big todo
@Component
@RequiredArgsConstructor
@Slf4j
public class ManagerRotator {

    private final Settings settings;

    private final ManagerRepository managerRepository;

    private Manager[] managers;

    public void reloadData() {
        //@todo list?
        List<Manager> all = managerRepository.findAll();

        managers = new Manager[all.size()];

        managers = all.toArray(managers);

        for (int i = 0; i < managers.length; i++) {
            managers[i].setIndex(i);
        }
    }

    @PostConstruct
    private void init() {
        reloadData();
    }

    public Manager getNextManager(Profile profile) {

        reloadData();

        incrementIndex(profile, settings.getManagerIndex(profile));

        return getCurrentManager(profile);
    }

    private void incrementIndex(Profile profile, int startIndex) {

        reloadData();

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
        reloadData();

        int index = settings.getManagerIndex(profile);

        return managers[index];
    }

    public Manager[] getManagers() {

        reloadData();

        return managers;
    }
}
