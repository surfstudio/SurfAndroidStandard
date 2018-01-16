package ru.surfstudio.android.core.app.interactor.common.cache.file;

public interface ObjectConverter<T> {

    byte[] encode(T value);

    T decode(byte[] rawValue);
}
