package taplinkbot.telegram;

import taplinkbot.bot.Profile;

import java.util.Arrays;

//todo Validation annotations
public class Message {

    public String sourceText;

    public String chatId;

    public String[] args;

    public String cammand;

    public Profile profile;

    public final static String noArgumentValue = "нет аргумента";
    public String arg1;

    public String arg2;

    public static String[] getFilledArgs(Message msg) {
        String[] args = new String[4];

        Arrays.fill(args, noArgumentValue);

        for (int i = 0; i < msg.args.length; i++) {
            args[i] = msg.args[i];
        }
        return args;
    }

    /**
     * @return
     * @todo use @ToString from lombok
     */
    @Override
    public String toString() {

        String str;
        str = "text:" + sourceText + ", len: " + args.length + "\r\n";

        for (String arg : args) {
            str += arg + "\r\n";
        }
        return str;
    }

}
