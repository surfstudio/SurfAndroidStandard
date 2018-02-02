package ru.surfstudio.android.network.etag.storage;

import javax.inject.Inject;
import javax.inject.Named;

import ru.surfstudio.android.dagger.scope.PerApplication;
import ru.surfstudio.android.filestorage.BaseTextLocalCache;
import ru.surfstudio.android.filestorage.CacheConstant;
import ru.surfstudio.android.filestorage.naming.Sha256NamingProcessor;
import ru.surfstudio.android.filestorage.processor.CacheFileProcessor;


@PerApplication
final class EtagCache extends BaseTextLocalCache {

    private static final int CACHE_SIZE = 5000;

    @Inject
    public EtagCache(@Named(CacheConstant.INTERNAL_CACHE_DIR_DAGGER_NAME) final String cacheDir) {
        super(new CacheFileProcessor(cacheDir, "etag", CACHE_SIZE), new Sha256NamingProcessor());
    }
}
