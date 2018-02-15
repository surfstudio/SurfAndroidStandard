package ru.surfstudio.android.network;

/**
 * Интерфейс, указывающий что объект может быть трансформирован в объект типа T
 *
 * @param <T>
 */
public interface Transformable<T> {
    T transform();
}
