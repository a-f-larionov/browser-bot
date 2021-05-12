//FIN
package taplinkbot.telegram;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Интерфейс комманд телеграмм бота
 */
public abstract class Command {

    /**
     * Имя комманды, заполняется автоматически из name аннотации.
     * см. TelegramCommandAnnotationBeanPostProcessor
     */
    @Setter(AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    private String name = "";

    /**
     * Возвращает краткое описание действий команды
     *
     * @return краткое описание действий команды
     */
    public abstract String getDescription();

    /**
     * Вызывается при запросе выполнения команды
     *
     * @param msg сообщение от пользователя системы.
     * @return Ответ работы команды
     */
    public abstract Response run(Message msg);
}
