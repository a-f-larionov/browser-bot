package taplinkbot.telegram.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taplinkbot.bot.Actions;
import taplinkbot.telegram.Command;
import taplinkbot.telegram.CommandInterface;
import taplinkbot.telegram.Message;
import taplinkbot.telegram.Response;

@Component
@Command(name = "/get_number")
@RequiredArgsConstructor
public class GetNumber implements CommandInterface {

    private final Actions actions;

    @Override
    public Response run(Message msg) throws Exception {

        long start = System.currentTimeMillis();
        String phoneNumber;

        phoneNumber = actions.getNumber(msg.profile);
        long finish = System.currentTimeMillis();

        return new Response("Номер телефона: " + phoneNumber + ", " + (finish - start) + " мсек.");
    }
}
