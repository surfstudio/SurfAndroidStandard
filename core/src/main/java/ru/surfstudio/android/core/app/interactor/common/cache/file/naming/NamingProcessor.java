package ru.surfstudio.android.core.app.interactor.common.cache.file.naming;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Интерфейс, описывающий контракт конвертирования
 */
public interface NamingProcessor {
    /**
     * Метод, который конвертирует требуемую строку в необходимый тип
     *
     * @param rawName - исходная строка (не может быть null)
     * @return - Кодированная строка исходя из имплементации класса
     */
    @Nullable
    String getNameFrom(@NotNull String rawName);
}
