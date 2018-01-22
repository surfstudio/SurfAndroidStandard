package ru.surfstudio.android.network.cache;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.surfstudio.android.core.app.interactor.common.cache.file.BaseTextLocalCache;
import ru.surfstudio.android.core.app.interactor.common.cache.file.naming.NamingProcessor;
import ru.surfstudio.android.core.app.interactor.common.cache.file.naming.Sha256NamingProcessor;
import ru.surfstudio.android.core.app.interactor.common.cache.file.naming.SimpleNamingProcessor;
import ru.surfstudio.android.core.app.interactor.common.cache.file.processor.CacheFileProcessor;


/**
 * простой кеш, кеширует сырой ответ сервера
 * если размер кеша равен 1, данные содержатся в файле с именем cacheDirName внутри папки cacheDirName
 */
public class SimpleCache extends BaseTextLocalCache {

    private static final NamingProcessor simpleNamingProcessor = new SimpleNamingProcessor();
    private static final NamingProcessor sha256NamingProcessor = new Sha256NamingProcessor();

    public SimpleCache(String cacheDir, String cacheDirName, int maxSize) {
        super(new CacheFileProcessor(cacheDir,
                        simpleNamingProcessor.getNameFrom(cacheDirName), maxSize),
                maxSize == 1 ? new SingleFileNamingProcessor(cacheDirName) : sha256NamingProcessor);
    }

    private static class SingleFileNamingProcessor implements NamingProcessor {
        private String singleFileName;

        public SingleFileNamingProcessor(String singleFileName) {
            this.singleFileName = singleFileName;
        }

        @Nullable
        @Override
        public String getNameFrom(@NotNull String rawName) {
            //любая запись перезаписывает единственный файл
            return singleFileName;
        }
    }
}
