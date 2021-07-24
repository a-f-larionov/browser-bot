
package browserbot.services;

import browserbot.BrowserBotException;
import browserbot.bots.taplink.Profile;
import browserbot.entities.Manager;
import browserbot.repositories.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ManagerRotator {

    private final Settings settings;

    private final ManagerRepository managerRepository;

    public Manager getCurrentManager(Profile profile) {

        return managerRepository.findById(settings.getManagerId(profile)).orElseThrow(() -> new BrowserBotException("Manager not found"));
    }

    /**
     * Установить следующего менеджера
     *
     * @param profile
     * @param fromId
     */
    public void setNextManager(Profile profile, long fromId) {

        Manager manager = getManagerAfterId(fromId);

        // поиск остановиться если мы вернулись к началу или нашли рабочего менеджера
        while (manager.getId() != fromId && !manager.isWorking()) {
            manager = getManagerAfterId(manager.getId());
        }

        settings.setManagerIndex(profile, manager.getId());
    }

    /**
     * Возвращает следующего менеджера
     *
     * @param id
     * @return
     */
    private Manager getManagerAfterId(Long id) {

        Long nextId = managerRepository.getAfterId(id);

        if (nextId == null) nextId = managerRepository.getFirstId();

        return managerRepository.findById(nextId).orElseThrow(() -> new BrowserBotException("Manager does not exists"));
    }
}
