package ru.surfstudio.android.core.util;

/**
 * Runnable c информацией
 */
public interface RunnableWithData <T> {
    void run(T data);
}
