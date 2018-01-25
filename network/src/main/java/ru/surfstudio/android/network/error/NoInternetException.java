package ru.surfstudio.android.network.error;

/**
 * отсутствует подключение к интернету
 */
public class NoInternetException extends NetworkException {

    public NoInternetException(Throwable e) {
        super(e);
    }
}
