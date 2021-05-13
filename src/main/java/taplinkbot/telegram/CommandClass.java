//FIN
package taplinkbot.telegram;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для обозначения комманд телеграмма
 * смотри TelegramCommandInterface
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandClass {

    /**
     * Имя команды /command-name
     *
     * @return String имя команды
     */
    String name();
}
