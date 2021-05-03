package taplinkbot.entities;

import org.hibernate.annotations.CreationTimestamp;
import taplinkbot.bot.BotContext;

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

    private BotContext botContext;

    public PhoneLogger(String phoneNumber, BotContext botContext) {
        this.phoneNumber = phoneNumber;
        this.botContext = botContext;
    }

    public PhoneLogger() {

    }
}
