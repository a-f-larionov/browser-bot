package taplinkbot.telegram;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CommandAnnotationBeanPostProcessor implements BeanPostProcessor {

    private Map<String, Command> beans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if (bean.getClass().isAnnotationPresent(TelegramCommand.class)) {
            beans.put(beanName, (Command) bean);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Command command = beans.get(beanName);

        if (command != null) {
            String commandName = bean.getClass().getAnnotation(TelegramCommand.class).name();
            command.setName(commandName);
            Commands.addCommand(commandName, command);
        }

        return bean;
    }
}
