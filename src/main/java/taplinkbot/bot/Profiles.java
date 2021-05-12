package taplinkbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Slf4j
public class Profiles {

    private Profile profile;

    public static Profile findByName(String arg) {

        for (Profile botProfile : Profile.values()) {
            if (botProfile.name.equals(arg) ||
                    botProfile.alias.equals(arg)) {
                return botProfile;
            }
        }

        return null;
    }

    /**
     * @return
     * @todo strange code...
     */
    public static String getValuesCommaString() {

        return Arrays.asList(Profile.values())
                .stream()
                .map((Profile profile) -> {
                    return profile.name;
                })
                .collect(Collectors.joining(","));
    }
}
