package ru.surfstudio.standard.interactor.common.error;

/**
 * Ошибка если с сервера приходит неизвестный enum
 */

public class UnknownServerEnum extends IllegalArgumentException {
    private static final String ERROR_MESSAGE = "Неизвестное значение enum: ";

    public UnknownServerEnum(int value) {
        super(ERROR_MESSAGE + value);
    }

    public UnknownServerEnum(Class clazz, int value) {
        super(ERROR_MESSAGE + value + " для класса " + clazz.getName());
    }

    public UnknownServerEnum(Class clazz, String value) {
        super(ERROR_MESSAGE + value + " для класса " + clazz.getName());
    }
}
