//FIN
package taplinkbot.components;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Ответственне за мультиязычость и централизацию текстовых сообщений.
 */
@Component
@RequiredArgsConstructor
public class LangComponent {

    private final MessageSource messageSource;

    public String get(String s) {
        return get(s, (Object) null);
    }

    public String get(String s, Object... args) {
        return messageSource.getMessage(s, args, LocaleContextHolder.getLocale());
    }
}
