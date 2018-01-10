package ru.surfstudio.standard.interactor.common.error;

/**
 * Ошибка возникающая при попытке инстанцирования класса где это запрещено. (Например Utils классы)
 */

public class NonInstanceClassCreateException extends RuntimeException {

    public NonInstanceClassCreateException(Class clazz) {
        super(clazz.getName() + " не может иметь инстанс");
    }
}
