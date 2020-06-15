package ru.surfstudio.android.sample.dagger.app.exceptions;

public class NoInternetException extends NetworkException {

    public NoInternetException(Throwable e) {
        super(e);
    }
}
