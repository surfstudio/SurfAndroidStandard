package ru.surfstudio.android.network.error;


/**
 * получен ответ не 2xx
 */
public abstract class HttpError extends NetworkException {
    private final int code;

    public HttpError(Throwable cause, int code, String url) {
        super(prepareMessage(code, url, 0), cause);
        this.code = code;
    }

    private static String prepareMessage(int code, String url, int innerCode) {
        return " httpCode=" + code + "\n" +
                ", url='" + url + "'" + "\n" +
                ", innerCode=" + innerCode;
    }

    public int getCode() {
        return code;
    }
}
