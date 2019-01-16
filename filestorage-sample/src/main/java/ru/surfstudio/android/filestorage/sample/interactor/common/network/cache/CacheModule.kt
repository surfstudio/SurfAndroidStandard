package ru.surfstudio.android.filestorage.sample.interactor.common.network.cache

import android.content.Context
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.CacheConstant
import ru.surfstudio.android.filestorage.utils.AppDirectoriesProvider
import ru.surfstudio.android.network.BaseUrl
import ru.surfstudio.android.network.cache.SimpleCacheFactory
import ru.surfstudio.android.network.cache.SimpleCacheInfo
import ru.surfstudio.android.network.cache.SimpleCacheInterceptor
import ru.surfstudio.android.network.cache.SimpleCacheUrlConnector
import ru.surfstudio.android.utilktx.util.java.CollectionUtils
import java.util.*
import javax.inject.Named

@Module
class CacheModule {

    @Provides
    @PerApplication
    @Named(CacheConstant.INTERNAL_CACHE_DIR_DAGGER_NAME)
    fun provideInternalCacheDir(context: Context): String {
        return ContextCompat.getNoBackupFilesDir(context)!!.absolutePath
    }

    @Provides
    @PerApplication
    @Named(CacheConstant.EXTERNAL_CACHE_DIR_DAGGER_NAME)
    fun provideExternalCacheDir(context: Context): String {
        val externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null)
        // могут возвращаться null элементы, убираем их
        val filtered = CollectionUtils.filter(Arrays.asList(*externalFilesDirs)) { file -> file != null }
        // берем последний из списка
        val result = CollectionUtils.last(filtered)
        // если подходящего элемента не оказалось, берем директорию внутреннего кэша
        return if (result != null) result.absolutePath else provideInternalCacheDir(context)
    }

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

    private fun simpleCacheInfoList(): Collection<SimpleCacheInfo> = listOf()
}