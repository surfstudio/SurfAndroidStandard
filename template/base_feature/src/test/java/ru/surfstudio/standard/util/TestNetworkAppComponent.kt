package ru.surfstudio.standard.util

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.application.app.di.AppModule
import ru.surfstudio.standard.application.auth.di.AuthModule
import ru.surfstudio.standard.application.network.di.NetworkModule
import ru.surfstudio.standard.application.network.di.OkHttpModule
import ru.surfstudio.standard.application.cache.di.CacheModule

@PerApplication
@Component(modules = [
    AppModule::class,
    AuthModule::class,
    NetworkModule::class,
    OkHttpModule::class,
    CacheModule::class])
interface TestNetworkAppComponent {
    fun inject(test: BaseNetworkDaggerTest)
}