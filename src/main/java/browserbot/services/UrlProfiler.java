
package browserbot.services;

import browserbot.entities.PageLoadTime;
import browserbot.repositories.URLProfilerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Компонент профилирует время загрузки страниц.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class UrlProfiler {

    private final URLProfilerRepository URLProfilerRepository;

    private long start;

    private boolean inProgress = false;

    /**
     * Начианет отсчёт времени перед запросом страницы.
     */
    public void start() {
        if (inProgress) {
            inProgress = false;
            log.info("Вложенный профайлинг старт");
        }
        inProgress = true;

        start = System.currentTimeMillis();
    }

    /**
     * Завершает и фиксирует время потраченное не запрос страницы.
     *
     * @param url URL проилируемой страницы
     */
    public void finish(String url) {
        if (!inProgress) {
            log.error("Вложенный профайлинг финиш");
            return;
        }
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

        URLProfilerRepository.save(new PageLoadTime(url, finish - start));
    }
}
