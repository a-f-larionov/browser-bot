//FIN
package taplinkbot.telegram;

/**
 * Интерфейс комманд телеграмм бота
 */
public interface CommandInterface {

    /**
     * Возвращает краткое описание действий команды
     *
     * @return краткое описание действий команды
     */
    String getDescription();

    /**
     * Вызывается при запросе выполнения команды
     *
     * @param msg сообщение от пользователя системы.
     * @return Ответ работы команды
     */
    Response run(Message msg);
}
