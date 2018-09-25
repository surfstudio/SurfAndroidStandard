package ru.surfstudio.android.filestorage.sample.interactor.common.network

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.utils.AppDirectoriesProvider
import ru.surfstudio.android.network.etag.EtagInterceptor
import ru.surfstudio.android.network.etag.storage.EtagCache
import ru.surfstudio.android.network.etag.storage.EtagStorage

@Module
class EtagModule {

    @Provides
    @PerApplication
    internal fun provideEtagCache(appDirectoriesProvider: AppDirectoriesProvider): EtagCache {
        return EtagCache(appDirectoriesProvider.provideInternalCacheDir())
    }

    @Provides
    @PerApplication
    internal fun provideEtagStorage(etagCache: EtagCache): EtagStorage = EtagStorage(etagCache)

    @Provides
    @PerApplication
    internal fun provideEtagInterceptor(etagStorage: EtagStorage): EtagInterceptor {
        return EtagInterceptor(etagStorage)
    }
}