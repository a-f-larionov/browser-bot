package taplinkbot.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taplinkbot.bot.Profile;
import taplinkbot.entities.PhoneLogger;
import taplinkbot.repositories.PhoneLoggerRepository;

/**
 * Контроль номеров телефонов
 */
@Service
@RequiredArgsConstructor
public class PhoneControlService {

    private final PhoneLoggerRepository phoneLoggerRepository;

    public void save(String phoneNumber, Profile profile) {

        phoneLoggerRepository.save(new PhoneLogger(phoneNumber, profile));
    }
}
