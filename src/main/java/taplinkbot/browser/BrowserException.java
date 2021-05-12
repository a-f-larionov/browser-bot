//FIN
package taplinkbot.browser;

import taplinkbot.TapLinkBotException;

/**
 * Исключения содержат данные браузера для отладки.
 */
public class BrowserException extends TapLinkBotException {

    private String screenShotUrl = "";

    private String comment = "";

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
