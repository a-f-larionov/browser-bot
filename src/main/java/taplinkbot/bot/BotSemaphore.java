package taplinkbot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotSemaphore {

    private boolean locked = false;

    @Autowired
    private DriverWrapper wrapper;

    public boolean lock() {
        if (locked) return false;
        locked = true;
        return true;
    }

    public void unlock() {
        locked = false;
    }

    public boolean isntLocked() {
        return !locked;
    }
}
