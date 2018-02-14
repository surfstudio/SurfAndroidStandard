package ru.surfstudio.android.core.util;

/**
 * Интерфейс, указывающий что объект может быть трансформирован в объект типа T
 *
 * @param <T>
 */
public interface Transformable<T> {
    T transform();
}
