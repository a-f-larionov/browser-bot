//FIN
package browserbot.entities;

import browserbot.bots.taplink.Profile;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Entity
public class PhoneLogger {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private LocalDateTime localDateTime;

    @Pattern(regexp = "^\\+7\\d{10}$")
    private String phone;

    @NotNull
    private Profile profile;

    public PhoneLogger(String phone, Profile profile) {

        this.phone = phone;
        this.profile = profile;
    }

    public PhoneLogger() {

    }
}
