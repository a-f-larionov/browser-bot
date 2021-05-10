package taplinkbot.browser;

public class BrowserException extends RuntimeException {

    private String screenShotUrl;

    private String lastComment;

    public BrowserException(String message, String screenShotUrl, String lastComment) {
        super(message);
        this.screenShotUrl = screenShotUrl;
        this.lastComment = lastComment;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " Комментарий: " + lastComment + " Скриншот: " + screenShotUrl;
    }
}
