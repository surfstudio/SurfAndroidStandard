package ru.surfstudio.standard.interactor.common.error;

/**
 * отсутствует подключение к интернету
 */
public class NoInternetException extends NetworkException {

    public NoInternetException(Throwable e) {
        super(e);
    }
}
