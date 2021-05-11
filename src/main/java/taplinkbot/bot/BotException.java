package taplinkbot.bot;

import taplinkbot.browser.Browser;
import taplinkbot.browser.BrowserException;

public class BotException extends BrowserException {

    public BotException(String message) {
        super(message);
    }

    public BotException(String message, Browser browser) {
        super(message, browser);
    }

    public BotException(String message, String screenShotUrl, String lastComment) {
        super(message, screenShotUrl, lastComment);
    }
}
