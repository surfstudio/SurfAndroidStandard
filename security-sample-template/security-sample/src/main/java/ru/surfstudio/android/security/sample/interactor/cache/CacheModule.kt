package ru.surfstudio.android.security.sample.interactor.cache

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.CacheConstant
import ru.surfstudio.android.filestorage.utils.AppDirectoriesProvider.provideCacheDir
import ru.surfstudio.android.filestorage.utils.AppDirectoriesProvider.provideNoBackupStorageDir
import ru.surfstudio.android.network.BaseUrl
import ru.surfstudio.android.network.cache.SimpleCacheInfo
import ru.surfstudio.android.network.cache.SimpleCacheUrlConnector
import javax.inject.Named

@Module
class CacheModule {

    @Provides
    @PerApplication
    @Named(CacheConstant.INTERNAL_CACHE_DIR_DAGGER_NAME)
    fun provideInternalCacheDir(context: Context): String {
        return provideNoBackupStorageDir(context)
    }

    @Provides
    @PerApplication
    @Named(CacheConstant.EXTERNAL_CACHE_DIR_DAGGER_NAME)
    fun provideExternalCacheDir(context: Context): String {
        return provideCacheDir(context)
    }

    @Provides
    @PerApplication
    fun provideSimpleCacheUrlConnector(baseUrl: BaseUrl): SimpleCacheUrlConnector {
        return SimpleCacheUrlConnector(baseUrl, simpleCacheInfoList())
    }

    private fun simpleCacheInfoList(): Collection<SimpleCacheInfo> = listOf()
}