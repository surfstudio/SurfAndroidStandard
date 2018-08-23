package ru.surfstudio.android.network.sample.app.dagger

import android.content.Context
import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.network.sample.interactor.common.network.NetworkModule
import ru.surfstudio.android.network.sample.interactor.common.network.OkHttpModule
import ru.surfstudio.android.network.sample.interactor.common.network.ServerUrlModule
import ru.surfstudio.android.network.sample.interactor.common.network.cache.CacheModule
import ru.surfstudio.android.network.sample.interactor.product.ProductModule
import ru.surfstudio.android.network.sample.interactor.product.ProductRepository
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultSharedPrefModule

@PerApplication
@Component(modules = [
    DefaultAppModule::class,
    DefaultSharedPrefModule::class,
    CacheModule::class,
    NetworkModule::class,
    OkHttpModule::class,
    ServerUrlModule::class,
    ProductModule::class])
interface AppComponent {
    fun context(): Context
    fun activeActivityHolder(): ActiveActivityHolder
    fun connectionProvider(): ConnectionProvider
    fun schedulerProvider(): SchedulersProvider
    fun stringsProvider(): StringsProvider
    fun productRepository(): ProductRepository
}
