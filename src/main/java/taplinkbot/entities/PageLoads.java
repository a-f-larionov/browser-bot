package taplinkbot.entities;

import org.hibernate.annotations.CreationTimestamp;
import taplinkbot.bot.BotContext;;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class PageLoads {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @CreationTimestamp
    private LocalDateTime localDateTime;

    private String url;

    private long loadTime;

    private BotContext botContext;

    public PageLoads() {

    }

    public PageLoads(String url, long loadTime, BotContext botContext) {
        this.loadTime = loadTime;
        this.url = url;
        this.botContext = botContext;
    }
}
