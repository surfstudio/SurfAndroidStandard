package ru.surfstudio.android.filestorage;

public interface ObjectConverter<T> {

    byte[] encode(T value);

    T decode(byte[] rawValue);
}
