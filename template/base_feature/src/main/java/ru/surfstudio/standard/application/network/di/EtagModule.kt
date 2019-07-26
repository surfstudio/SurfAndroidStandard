package ru.surfstudio.standard.application.network.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.utils.AppDirectoriesProvider
import ru.surfstudio.android.network.etag.EtagInterceptor
import ru.surfstudio.android.network.etag.storage.EtagStorage
import ru.surfstudio.android.network.etag.storage.EtagCache

@Module
class EtagModule {

    @Provides
    @PerApplication
    internal fun provideEtagCache(context: Context): EtagCache {
        return EtagCache(AppDirectoriesProvider.provideNoBackupStorageDir(context))
    }

    @Provides
    @PerApplication
    internal fun provideEtagStorage(etagCache: EtagCache): EtagStorage {
        return EtagStorage(etagCache)
    }

    @Provides
    @PerApplication
    internal fun provideEtagInterceptor(etagStorage: EtagStorage): EtagInterceptor {
        return EtagInterceptor(etagStorage)
    }
}