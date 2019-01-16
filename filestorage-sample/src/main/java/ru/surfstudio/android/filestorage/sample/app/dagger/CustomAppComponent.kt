package ru.surfstudio.android.filestorage.sample.app.dagger

import android.content.Context
import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.core.app.StringsProvider
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
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultSharedPrefModule

@PerApplication
@Component(
        modules = [
            CustomAppModule::class,
            EtagModule::class,
            CacheModule::class,
            NetworkModule::class,
            OkHttpModule::class,
            DefaultSharedPrefModule::class,
            ServerUrlModule::class,
            IpModule::class])
interface CustomAppComponent {
    fun context(): Context
    fun activeActivityHolder(): ActiveActivityHolder
    fun connectionProvider(): ConnectionProvider
    fun schedulerProvider(): SchedulersProvider
    fun stringsProvider(): StringsProvider

    fun ipRepository(): IpRepository
    fun ipJsonStorageWrapper(): IpJsonStorageWrapper
    fun ipSerializableStorageWrapper(): IpSerializableStorageWrapper
}
