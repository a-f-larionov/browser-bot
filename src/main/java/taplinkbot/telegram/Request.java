package taplinkbot.telegram;

import taplinkbot.bot.Profile;

//todo Validation annotations
public class Request {

    public String command;

    public String arg1;

    public String arg2;

    public String chatId;

    public Profile profile;

    public String[] args;

    public final static String noArgumentValue = "нет аргумента";

    /**
     * @return
     * @todo use @ToString from lombok
     */
    @Override
    public String toString() {

        String str;
        str = "";

        if (command != null) str += command + " ";
        if (arg1 != null) str += arg1 + " ";
        if (arg2 != null) str += arg2 + " ";

        return str;
    }

}