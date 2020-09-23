package ru.surfstudio.android.sample.dagger.app.exceptions;

/**
 * Base class for all errors during server interaction
 */
public abstract class NetworkException extends RuntimeException {
    public NetworkException() {
    }

    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(Throwable cause) {
        super(cause);
    }

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
