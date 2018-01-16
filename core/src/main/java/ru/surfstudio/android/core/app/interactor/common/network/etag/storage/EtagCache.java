package ru.surfstudio.android.core.app.interactor.common.network.etag.storage;

import javax.inject.Inject;
import javax.inject.Named;

import ru.surfstudio.android.core.app.dagger.scope.PerApplication;
import ru.surfstudio.android.core.app.interactor.common.cache.file.BaseTextLocalCache;
import ru.surfstudio.android.core.app.interactor.common.cache.file.CacheConstant;
import ru.surfstudio.android.core.app.interactor.common.cache.file.naming.Sha256NamingProcessor;
import ru.surfstudio.android.core.app.interactor.common.cache.file.processor.CacheFileProcessor;


@PerApplication
final class EtagCache extends BaseTextLocalCache {

    private static final int CACHE_SIZE = 5000;

    @Inject
    public EtagCache(@Named(CacheConstant.INTERNAL_CACHE_DIR_DAGGER_NAME) final String cacheDir) {
        super(new CacheFileProcessor(cacheDir, "etag", CACHE_SIZE), new Sha256NamingProcessor());
    }
}
