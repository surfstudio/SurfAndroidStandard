package ru.surfstudio.android.filestorage.sample.app.dagger

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.sample.interactor.common.network.EtagModule
import ru.surfstudio.android.filestorage.sample.interactor.common.network.NetworkModule
import ru.surfstudio.android.filestorage.sample.interactor.common.network.OkHttpModule
import ru.surfstudio.android.filestorage.sample.interactor.common.network.ServerUrlModule
import ru.surfstudio.android.filestorage.sample.interactor.common.network.cache.CacheModule
import ru.surfstudio.android.filestorage.sample.interactor.ip.IpModule
import ru.surfstudio.android.filestorage.sample.interactor.ip.IpRepository
import ru.surfstudio.android.filestorage.sample.interactor.ip.cache.IpJsonStorageWrapper
import ru.surfstudio.android.filestorage.sample.interactor.ip.cache.IpSerializableStorageWrapper
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultSharedPrefModule
import ru.surfstudio.android.sample.dagger.app.dagger.NavigationModule

@PerApplication
@Component(
    modules = [
        DefaultAppModule::class,
        NavigationModule::class,
        EtagModule::class,
        CacheModule::class,
        NetworkModule::class,
        OkHttpModule::class,
        DefaultSharedPrefModule::class,
        ServerUrlModule::class,
        IpModule::class
    ]
)
interface CustomAppComponent : DefaultAppComponent {
    fun ipRepository(): IpRepository
    fun ipJsonStorageWrapper(): IpJsonStorageWrapper
    fun ipSerializableStorageWrapper(): IpSerializableStorageWrapper
}