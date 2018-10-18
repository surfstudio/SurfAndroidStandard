package ru.surfstudio.android.filestorage.sample.interactor.common.network.cache

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.utils.AppDirectoriesProvider
import ru.surfstudio.android.network.BaseUrl
import ru.surfstudio.android.network.cache.SimpleCacheFactory
import ru.surfstudio.android.network.cache.SimpleCacheInfo
import ru.surfstudio.android.network.cache.SimpleCacheInterceptor
import ru.surfstudio.android.network.cache.SimpleCacheUrlConnector

@Module
class CacheModule {

    @Provides
    @PerApplication
    internal fun provideSimpleCacheInterceptor(
            simpleCacheFactory: SimpleCacheFactory,
            simpleCacheUrlConnector: SimpleCacheUrlConnector
    ): SimpleCacheInterceptor {
        return SimpleCacheInterceptor(simpleCacheFactory, simpleCacheUrlConnector)
    }

    @Provides
    @PerApplication
    internal fun provideSimpleCacheFactory(
            appDirectoriesProvider: AppDirectoriesProvider,
            cacheUrlConnector: SimpleCacheUrlConnector
    ): SimpleCacheFactory {
        return SimpleCacheFactory(appDirectoriesProvider.provideExternalCacheDir(), cacheUrlConnector)
    }

    @Provides
    @PerApplication
    internal fun provideAppDirectoriesProvider(context: Context): AppDirectoriesProvider {
        return AppDirectoriesProvider(context)
    }

    @Provides
    @PerApplication
    internal fun provideSimpleCacheUrlConnector(baseUrl: BaseUrl): SimpleCacheUrlConnector {
        return SimpleCacheUrlConnector(baseUrl, simpleCacheInfoList())
    }

    private fun simpleCacheInfoList(): Collection<SimpleCacheInfo> = listOf()
}