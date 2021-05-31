package taplinkbot.components;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LangComponent {

    private final MessageSource messageSource;

    LangComponent(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String get(String s) {
        return messageSource.getMessage(s, null, LocaleContextHolder.getLocale());
    }

}
