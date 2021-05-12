package taplinkbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

//@Todo
@Component
@Slf4j
public class Profiles {

    public static Profile findByName(String arg) {

        for (Profile profile : Profile.values()) {
            if (profile.name.equals(arg) ||
                    profile.alias.equals(arg)) {
                return profile;
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
