package taplinkbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Slf4j
public class Profiles {

    private Profile profile;

    public void set(Profile profile) {

        if (profile == null) {
            log.info("profile is NULL!!!");
        }

        if (profile != null && this.profile != null) {
//            log.info("bot context is busy by:" + this.profile.name
//                    + "!!! Requested: " + botProfile.name);
        }

        this.profile = profile;
    }

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

    public void clear() {
        this.profile = null;
    }
}
