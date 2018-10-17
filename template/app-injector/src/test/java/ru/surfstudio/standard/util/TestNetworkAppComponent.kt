package ru.surfstudio.standard.util

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.app_injector.AppModule
import ru.surfstudio.standard.app_injector.interactor.AuthModule
import ru.surfstudio.standard.app_injector.network.NetworkModule
import ru.surfstudio.standard.app_injector.network.cache.CacheModule
import ru.surfstudio.standard.util.modules.TestHttpLoggingModule
import ru.surfstudio.standard.util.modules.TestOkHttpModule

@PerApplication
@Component(modules = [
    AppModule::class,
    AuthModule::class,
    NetworkModule::class,
    TestHttpLoggingModule::class,
    TestOkHttpModule::class,
    CacheModule::class])
interface TestNetworkAppComponent {
    fun inject(test: BaseNetworkDaggerTest)
}