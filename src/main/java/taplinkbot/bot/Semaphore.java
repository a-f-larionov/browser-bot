package taplinkbot.bot;

import org.springframework.stereotype.Component;

@Component
public class Semaphore {

    private boolean locked = false;

    public boolean lock() {
        if (locked) return false;
        locked = true;
        return true;
    }

    public void unlock() {
        locked = false;
    }

    /**
     * @todo remvoe unsed methods
     * @return
     */
    public boolean isntLocked() {
        return !locked;
    }
}
