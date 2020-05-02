package ru.surfstudio.android.sample.dagger.app.exceptions;

/**
 * отсутствует подключение к интернету
 */
public class NoInternetException extends NetworkException {

    public NoInternetException(Throwable e) {
        super(e);
    }
}
