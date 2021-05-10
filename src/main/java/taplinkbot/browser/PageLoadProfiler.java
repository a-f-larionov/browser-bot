package taplinkbot.browser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Profile;
import taplinkbot.bot.Profiles;
import taplinkbot.entities.PageLoads;
import taplinkbot.repositories.PageLoadsRepository;

/**
 * Компонент профилирует время загрузки страниц.
 */
@Component
@RequiredArgsConstructor
public class PageLoadProfiler {

    private final Profiles profiles;

    private final PageLoadsRepository pageLoadsRepository;

    private long start = 0;

    private boolean inProgress = false;

    /**
     * Начианет отсчёт времени перед запросом страницы.
     */
    public void start() {
        if (inProgress) throw new Error("Вложенный профайлинг.");
        inProgress = true;

        start = System.currentTimeMillis();
    }

    /**
     * Завершает и фиксирует время потраченное не запрос страницы.
     *
     * @param url URL проилируемой страницы
     */
    public void finish(String url) {
        if (!inProgress) throw new Error("Завершение без начала. Нужно вызвать start() перед finish()");
        long finish = System.currentTimeMillis();

        logProfiling(url, start, finish);
        inProgress = false;
    }

    /**
     * Сохраняет в БД профайлинг запроса страницы.
     *
     * @param url    URL страницы
     * @param start  время начало запроса
     * @param finish время получения тела документа
     */
    private void logProfiling(String url, long start, long finish) {

        Profile profile = profiles.current();
        pageLoadsRepository.save(new PageLoads(url, finish - start, profile));
    }
}
