package ru.surfstudio.standard.interactor.common.error;

/**
 * ошибка парсинга ответа
 */
public class ConversionException extends NetworkException {
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConversionException(String message) {
        super(message);
    }
}
