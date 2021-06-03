package browserbot.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import browserbot.bots.taplink.Profile;
import browserbot.entities.PhoneLogger;
import browserbot.repositories.PhoneLoggerRepository;

/**
 * Контроль номеров телефонов
 */
@Service
@RequiredArgsConstructor
public class PhoneControlService {

    private final PhoneLoggerRepository phoneLoggerRepository;

    public void save(String phone, Profile profile) {

        phoneLoggerRepository.save(new PhoneLogger(phone, profile));
    }
}
