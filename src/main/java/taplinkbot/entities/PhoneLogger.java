package taplinkbot.entities;

import org.hibernate.annotations.CreationTimestamp;
import taplinkbot.bot.Context;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class PhoneLogger {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @CreationTimestamp
    private LocalDateTime localDateTime;

    private String phoneNumber;

    private Context botContext;

    public PhoneLogger(String phoneNumber, Context botContext) {
        this.phoneNumber = phoneNumber;
        this.botContext = botContext;
    }

    public PhoneLogger() {

    }
}
