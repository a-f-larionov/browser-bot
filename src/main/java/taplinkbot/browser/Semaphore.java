package taplinkbot.browser;

import org.springframework.stereotype.Component;

/**
 * Семафор для блокировок
 */
@Component
public class Semaphore {

    /**
     * Состояние блокировки
     */
    private boolean locked = false;

    /**
     * Устанавливает блокировку и возвращает true, если заблокировано возвращает false.
     *
     * @return true - успешно заблокировано, false - уже заблокировано
     */
    public boolean lock() {
        if (locked) return false;

        locked = true;

        return true;
    }

    public void unlock() {
        locked = false;
    }
}
