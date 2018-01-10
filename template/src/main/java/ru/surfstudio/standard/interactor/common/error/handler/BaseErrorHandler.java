package ru.surfstudio.standard.interactor.common.error.handler;

import ru.surfstudio.standard.interactor.common.error.HttpProtocolException;

/**
 * Базовый класс обработки ошибок сервера
 */

public interface BaseErrorHandler {

    void handle(HttpProtocolException e);
}
