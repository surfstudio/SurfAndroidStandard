package ru.surfstudio.android.security.sample.interactor.storage

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.CacheConstant
import ru.surfstudio.android.filestorage.naming.NamingProcessor
import ru.surfstudio.android.filestorage.processor.FileProcessor
import ru.surfstudio.android.filestorage.storage.BaseSerializableFileStorage
import ru.surfstudio.android.security.sample.domain.ApiKey
import javax.inject.Named

@Module
class ApiKeyModule {

    @Provides
    @PerApplication
    internal fun provideApiKeyCacheDirName(): String = "api_key_storage"

    @Provides
    @PerApplication
    internal fun provideFileProcessor(
            @Named(CacheConstant.INTERNAL_CACHE_DIR_DAGGER_NAME) cacheDir: String,
            cacheDirName: String
    ): FileProcessor {
        return FileProcessor(cacheDir, cacheDirName, 1)
    }

    @Provides
    @PerApplication
    internal fun provideIpNamingProcessor(): NamingProcessor {
        return NamingProcessor { rawName -> rawName }
    }

    @Provides
    @PerApplication
    internal fun provideTextStorage(
            fileProcessor: FileProcessor,
            namingProcessor: NamingProcessor
    ): BaseSerializableFileStorage<ApiKey> {
        return BaseSerializableFileStorage(fileProcessor, namingProcessor)
    }
}