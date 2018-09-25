package ru.surfstudio.android.filestorage.sample.interactor.ip

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.sample.interactor.ip.cache.IpStorage
import ru.surfstudio.android.filestorage.sample.interactor.ip.network.IpApi
import ru.surfstudio.android.filestorage.utils.AppDirectoriesProvider

@Module
class IpModule {

    @Provides
    @PerApplication
    internal fun provideIpStorage(appDirectoriesProvider: AppDirectoriesProvider): IpStorage {
        return IpStorage(appDirectoriesProvider.provideInternalCacheDir())
    }

    @Provides
    @PerApplication
    internal fun provideIpApi(retrofit: Retrofit): IpApi = retrofit.create(IpApi::class.java)
}