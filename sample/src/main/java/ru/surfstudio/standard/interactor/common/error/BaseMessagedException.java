package ru.surfstudio.standard.interactor.common.error;

import lombok.Data;

/**
 * Базовый класс ошибки
 */

@Data
public abstract class BaseMessagedException extends RuntimeException {

    private final String developerMessage;

    protected BaseMessagedException(String developerMessage) {
        this.developerMessage = developerMessage;
    }
}
