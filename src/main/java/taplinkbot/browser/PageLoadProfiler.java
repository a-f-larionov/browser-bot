package taplinkbot.browser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taplinkbot.entities.PageLoads;
import taplinkbot.repositories.PageLoadsRepository;

/**
 * Компонент профилирует время загрузки страниц.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PageLoadProfiler {

    private final PageLoadsRepository pageLoadsRepository;

    private long start = 0;

    private boolean inProgress = false;

    /**
     * Начианет отсчёт времени перед запросом страницы.
     */
    public void start() {
        //@todo test in Progress сбрасывается
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
            log.info("Вложенный профайлинг финиш");
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

        pageLoadsRepository.save(new PageLoads(url, finish - start));
    }
}
