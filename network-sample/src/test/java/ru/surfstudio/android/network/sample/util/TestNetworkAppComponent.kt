package ru.surfstudio.android.network.sample.util

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.network.sample.interactor.common.network.HttpLoggingModule
import ru.surfstudio.android.network.sample.interactor.common.network.NetworkModule
import ru.surfstudio.android.network.sample.interactor.common.network.OkHttpModule
import ru.surfstudio.android.network.sample.interactor.common.network.ServerUrlModule
import ru.surfstudio.android.network.sample.interactor.common.network.cache.CacheModule
import ru.surfstudio.android.network.sample.interactor.product.ProductModule
import ru.surfstudio.android.network.sample.interactor.product.ProductRepositoryTest
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule

@PerApplication
@Component(modules = [
    DefaultAppModule::class,
    NetworkModule::class,
    HttpLoggingModule::class,
    OkHttpModule::class,
    CacheModule::class,
    ServerUrlModule::class,
    ProductModule::class])
interface TestNetworkAppComponent {
    fun inject(test: BaseDaggerTest)
    fun inject(test: ProductRepositoryTest)
}