package ru.surfstudio.android.filestorage;

import org.jetbrains.annotations.NotNull;

import ru.surfstudio.android.filestorage.encryptor.Encryptor;
import ru.surfstudio.android.filestorage.naming.NamingProcessor;
import ru.surfstudio.android.filestorage.processor.FileProcessor;
import ru.surfstudio.android.filestorage.storage.BaseTextFileStorage;

/**
 * Базовый класс текстового кэша
 */
@Deprecated
public class BaseTextLocalCache extends BaseTextFileStorage {

    public BaseTextLocalCache(@NotNull FileProcessor fileProcessor, @NotNull NamingProcessor namingProcessor) {
        super(fileProcessor, namingProcessor);
    }

    public BaseTextLocalCache(@NotNull FileProcessor fileProcessor, @NotNull NamingProcessor namingProcessor, @NotNull Encryptor encryptor) {
        super(fileProcessor, namingProcessor, encryptor);
    }
}
