package ru.surfstudio.standard.util

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.app_injector.AppModule
import ru.surfstudio.standard.app_injector.interactor.AuthModule
import ru.surfstudio.standard.app_injector.interactor.storage.SharedPrefModule
import ru.surfstudio.standard.util.modules.TestCacheModule
import ru.surfstudio.standard.util.modules.TestNetworkModule
import ru.surfstudio.standard.util.modules.TestOkHttpModule

@PerApplication
@Component(modules = [
    AppModule::class,
    SharedPrefModule::class,
    AuthModule::class,
    TestNetworkModule::class,
    TestOkHttpModule::class,
    TestCacheModule::class])
interface TestNetworkAppComponent {
    fun inject(test: BaseDaggerTest)
}