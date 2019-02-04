package ru.surfstudio.android.filestorage.sample.interactor.ip

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.CacheConstant
import ru.surfstudio.android.filestorage.converter.JsonConverter
import ru.surfstudio.android.filestorage.naming.NamingProcessor
import ru.surfstudio.android.filestorage.processor.FileProcessor
import ru.surfstudio.android.filestorage.sample.domain.ip.Ip
import ru.surfstudio.android.filestorage.sample.interactor.ip.network.IpApi
import ru.surfstudio.android.filestorage.storage.BaseJsonFileStorage
import ru.surfstudio.android.filestorage.storage.BaseSerializableFileStorage
import javax.inject.Named

@Module
class IpModule {

    @Provides
    @PerApplication
    internal fun provideIpApi(retrofit: Retrofit): IpApi = retrofit.create(IpApi::class.java)

    @Provides
    @PerApplication
    internal fun provideIpObjectConverter(): JsonConverter<Ip> = JsonConverter(Ip::class.java)

    @Provides
    @PerApplication
    internal fun provideIpCacheDirName(): String = "ip_storage"

    @Provides
    @PerApplication
    internal fun provideFileProcessor(
            @Named(CacheConstant.INTERNAL_CACHE_DIR_DAGGER_NAME) cacheDir: String,
            cacheDirName: String
    ): FileProcessor {
        return FileProcessor(cacheDir, cacheDirName, 2)
    }

    @Provides
    @PerApplication
    internal fun provideNamingProcessor(): NamingProcessor {
        return NamingProcessor { rawName -> rawName }
    }

    @Provides
    @PerApplication
    internal fun provideIpJsonFileStorage(
            fileProcessor: FileProcessor,
            namingProcessor: NamingProcessor
    ): BaseJsonFileStorage<Ip> {
        return BaseJsonFileStorage(fileProcessor, namingProcessor, Ip::class.java)
    }

    @Provides
    @PerApplication
    internal fun provideIpSerializableFileStorage(
            fileProcessor: FileProcessor,
            ipNamingProcessor: NamingProcessor
    ): BaseSerializableFileStorage<Ip> {
        return BaseSerializableFileStorage(fileProcessor, ipNamingProcessor)
    }
}