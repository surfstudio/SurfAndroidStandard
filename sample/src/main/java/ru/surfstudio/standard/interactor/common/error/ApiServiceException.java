package ru.surfstudio.standard.interactor.common.error;

/**
 * ошибки сервиса (приходят в теле ответа)
 */
public class ApiServiceException extends NetworkException {
    private final int errorCode;
    private final String userMessage;

    public ApiServiceException(int errorCode, String userMessage) {

        this.errorCode = errorCode;
        this.userMessage = userMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getUserMessage() {
        return userMessage;
    }

    @Override
    public String toString() {
        return "ApiServiceException{" +
                "errorCode=" + errorCode +
                ", userMessage='" + userMessage + '\'' +
                '}';
    }
}
