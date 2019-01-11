package ru.surfstudio.standard.util

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.app_injector.AppModule
import ru.surfstudio.standard.app_injector.interactor.AuthModule
import ru.surfstudio.standard.app_injector.network.NetworkModule
import ru.surfstudio.standard.app_injector.network.OkHttpModule
import ru.surfstudio.standard.app_injector.network.cache.CacheModule

@PerApplication
@Component(modules = [
    AppModule::class,
    AuthModule::class,
    NetworkModule::class,
    OkHttpModule::class,
    CacheModule::class])
interface TestNetworkAppComponent {
    fun inject(test: BaseNetworkDaggerTest)
    fun inject(test: SampleApiTest)
}