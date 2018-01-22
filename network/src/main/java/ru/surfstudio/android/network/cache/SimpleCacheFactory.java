package ru.surfstudio.android.network.cache;


import com.annimon.stream.Stream;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import ru.surfstudio.android.core.app.dagger.scope.PerApplication;
import ru.surfstudio.android.core.app.interactor.common.cache.file.CacheConstant;

/**
 * фабрика простых кешей
 */
@PerApplication
public class SimpleCacheFactory {

    private final String cacheDir;
    private final BaseSimpleCacheUrlConnector cacheUrlConnector;
    private Map<SimpleCacheInfo, SimpleCache> caches = new HashMap<>();

    @Inject
    public SimpleCacheFactory(@Named(CacheConstant.EXTERNAL_CACHE_DIR_DAGGER_NAME) final String cacheDir,
                              BaseSimpleCacheUrlConnector cacheUrlConnector) {
        this.cacheDir = cacheDir;
        this.cacheUrlConnector = cacheUrlConnector;
    }

    public SimpleCache getSimpleCache(SimpleCacheInfo simpleCacheInfo) {
        SimpleCache cache = caches.get(simpleCacheInfo);
        if (cache == null) {
            cache = new SimpleCache(cacheDir, simpleCacheInfo.getCacheName(), simpleCacheInfo.getMaxSize());
            caches.put(simpleCacheInfo, cache);
        }
        return cache;
    }

    public void clearAllCache() {
        Stream.of(cacheUrlConnector.getSimpleCacheInfo())
                .map(this::getSimpleCache)
                .forEach(SimpleCache::clear);
    }
}
