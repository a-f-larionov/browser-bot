package browserbot.entities;

import org.hibernate.annotations.CreationTimestamp;
import browserbot.bots.taplink.Profile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

//@todo valdations
@Entity
public class PhoneLogger {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @CreationTimestamp
    private LocalDateTime localDateTime;

    private String phoneNumber;

    private Profile profile;

    public PhoneLogger(String phoneNumber, Profile profile) {

        this.phoneNumber = phoneNumber;
        this.profile = profile;
    }

    public PhoneLogger() {

    }
}
