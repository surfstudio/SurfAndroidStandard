package ru.surfstudio.android.filestorage;


import ru.surfstudio.android.filestorage.naming.NamingProcessor;
import ru.surfstudio.android.filestorage.processor.CacheFileProcessor;

/**
 * базовый класс текстового кеша
 */
public abstract class BaseTextLocalCache extends BaseLocalCache<String> {

    public BaseTextLocalCache(CacheFileProcessor fileProcessor, NamingProcessor namingProcessor) {
        super(fileProcessor, namingProcessor);
    }

    @Override
    ObjectConverter<String> getConverter() {
        return new ObjectConverter<String>() {
            @Override
            public byte[] encode(String value) {
                return value.getBytes();
            }

            @Override
            public String decode(byte[] rawValue) {
                return new String(rawValue);
            }
        };
    }
}
