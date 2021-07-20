package ru.surfstudio.android.network.sample.app.dagger

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.network.sample.interactor.common.network.EtagModule
import ru.surfstudio.android.network.sample.interactor.common.network.NetworkModule
import ru.surfstudio.android.network.sample.interactor.common.network.OkHttpModule
import ru.surfstudio.android.network.sample.interactor.common.network.ServerUrlModule
import ru.surfstudio.android.network.sample.interactor.common.network.cache.CacheModule
import ru.surfstudio.android.network.sample.interactor.product.ProductModule
import ru.surfstudio.android.network.sample.interactor.product.ProductRepository
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultSharedPrefModule
import ru.surfstudio.android.sample.dagger.app.dagger.NavigationModule

@PerApplication
@Component(
    modules = [
        DefaultAppModule::class,
        DefaultSharedPrefModule::class,
        NavigationModule::class,
        CacheModule::class,
        EtagModule::class,
        NetworkModule::class,
        OkHttpModule::class,
        ServerUrlModule::class,
        ProductModule::class
    ]
)
interface CustomAppComponent : DefaultAppComponent {
    fun productRepository(): ProductRepository
}
