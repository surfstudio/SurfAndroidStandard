package ru.surfstudio.android.core.util;

/**
 * Задает отображение K -> V
 */
public interface Mapper<K, V> {
    public V map(K key);
}