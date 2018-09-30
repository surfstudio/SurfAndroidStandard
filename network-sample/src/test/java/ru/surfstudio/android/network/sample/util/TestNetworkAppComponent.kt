package ru.surfstudio.android.network.sample.util

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.network.sample.interactor.common.network.NetworkModule
import ru.surfstudio.android.network.sample.interactor.common.network.ServerUrlModule
import ru.surfstudio.android.network.sample.interactor.product.ProductModule
import ru.surfstudio.android.network.sample.interactor.product.ProductRepositoryTest
import ru.surfstudio.android.network.sample.util.modules.TestHttpLoggingModule
import ru.surfstudio.android.network.sample.util.modules.TestOkHttpModule
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule

@PerApplication
@Component(modules = [
    DefaultAppModule::class,
    NetworkModule::class,
    TestHttpLoggingModule::class,
    TestOkHttpModule::class,
    ServerUrlModule::class,
    ProductModule::class])
interface TestNetworkAppComponent {
    fun inject(test: BaseDaggerTest)
    fun inject(test: ProductRepositoryTest)
}