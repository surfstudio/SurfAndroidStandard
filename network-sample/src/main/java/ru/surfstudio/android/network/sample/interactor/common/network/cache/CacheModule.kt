package ru.surfstudio.android.network.sample.interactor.common.network.cache

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.utils.AppDirectoriesProvider
import ru.surfstudio.android.network.BaseUrl
import ru.surfstudio.android.network.HttpMethods
import ru.surfstudio.android.network.cache.SimpleCacheFactory
import ru.surfstudio.android.network.cache.SimpleCacheInfo
import ru.surfstudio.android.network.cache.SimpleCacheInterceptor
import ru.surfstudio.android.network.cache.SimpleCacheUrlConnector
import ru.surfstudio.android.network.sample.interactor.common.network.ServerUrls.GET_PRODUCTS_URL

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
            context: Context,
            cacheUrlConnector: SimpleCacheUrlConnector
    ): SimpleCacheFactory {
        return SimpleCacheFactory(AppDirectoriesProvider.provideBackupStorageDir(context), cacheUrlConnector)
    }

    @Provides
    @PerApplication
    internal fun provideSimpleCacheUrlConnector(baseUrl: BaseUrl): SimpleCacheUrlConnector {
        return SimpleCacheUrlConnector(baseUrl, simpleCacheInfoList())
    }

    private fun simpleCacheInfoList(): Collection<SimpleCacheInfo> {
        return listOf(
                SimpleCacheInfo(
                        HttpMethods.GET,
                        GET_PRODUCTS_URL,
                        "products",
                        100)
        )
    }
}