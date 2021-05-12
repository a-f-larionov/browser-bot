package taplinkbot.browser;

import taplinkbot.TapLinkBotException;

public class BrowserException extends TapLinkBotException {

    private String screenShotUrl = "";

    private String comment = "";

    public BrowserException(String message) {
        super(message);
    }

    public BrowserException(String message, Browser browser) {
        super(message);
        this.comment = browser.getComment();
        this.screenShotUrl = browser.takeScreenshot();
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " Комментарий: " + comment + " Скриншот: " + screenShotUrl;
    }
}
