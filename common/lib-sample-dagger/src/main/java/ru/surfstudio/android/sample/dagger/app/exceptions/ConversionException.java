package ru.surfstudio.android.sample.dagger.app.exceptions;

/**
 * Response parsing error
 */
public class ConversionException extends NetworkException {
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConversionException(String message) {
        super(message);
    }
}
