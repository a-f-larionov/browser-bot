//FIN
package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Actions;
import taplinkbot.telegram.*;

@Component
@RequiredArgsConstructor
@TelegramCommand(name = "/get_number")
public class GetNumber extends Command {

    private final Actions actions;

    @Override
    public String getDescription() {
        return "Выведет установленный номер телефона.";
    }

    /**
     * Получить текущий номер на мульте-странице.
     */
    @Override
    public Response run(Message msg) {
        String phoneNumber;
        long start, finish;

        start = System.currentTimeMillis();

        phoneNumber = actions.getNumber(msg.profile);

        finish = System.currentTimeMillis();

        return ResponseFactory.buildSuccessReponse(
                "Номер телефона: " +
                        phoneNumber + ", " +
                        (finish - start) + " мсек."
        );
    }
}
