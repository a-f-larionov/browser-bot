//FIN
package browserbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import browserbot.bots.taplink.BotController;
import browserbot.telegram.*;

@Component
@RequiredArgsConstructor
@CommandClass(name = "/get_number")
public class GetNumber extends Command {

    private final BotController botController;

    @Override
    public String getDescription() {
        return "Выведет установленный номер телефона.";
    }

    /**
     * Получить текущий номер на мульте-странице.
     */
    @Override
    public Reponse run(Request msg) {
        String phoneNumber;
        long start, finish;

        start = System.currentTimeMillis();

        phoneNumber = botController.getNumber(msg.profile);

        finish = System.currentTimeMillis();

        return MessageBuilder.buildResult(
                "Номер телефона: " +
                        phoneNumber + ", " +
                        (finish - start) + " мсек."
        );
    }
}
