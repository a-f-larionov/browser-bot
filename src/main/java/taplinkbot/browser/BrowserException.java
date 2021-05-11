package taplinkbot.browser;

public class BrowserException extends RuntimeException {

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

    public BrowserException(String message, String screenShotUrl, String comment) {
        super(message);
        this.screenShotUrl = screenShotUrl;
        this.comment = comment;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " Комментарий: " + comment + " Скриншот: " + screenShotUrl;
    }
}
