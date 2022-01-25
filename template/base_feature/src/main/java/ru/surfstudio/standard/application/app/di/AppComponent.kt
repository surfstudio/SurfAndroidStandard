package ru.surfstudio.standard.application.app.di

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.i_network.generated.di.AuthModule
import ru.surfstudio.standard.application.cache.di.CacheModule
import ru.surfstudio.standard.application.migration.di.MigrationModule
import ru.surfstudio.standard.application.network.di.EtagModule
import ru.surfstudio.standard.application.network.di.NetworkModule
import ru.surfstudio.standard.application.network.di.OkHttpModule
import ru.surfstudio.standard.application.notification.FcmModule
import ru.surfstudio.standard.application.notification.MessagingService
import ru.surfstudio.standard.application.notification.NotificationModule
import ru.surfstudio.standard.application.storage.di.SharedPrefModule
import ru.surfstudio.standard.ui.navigation.di.NavigationModule

@PerApplication
@Component(modules = [
    AppModule::class,
    MigrationModule::class,
    SharedPrefModule::class,
    AuthModule::class,
    CacheModule::class,
    EtagModule::class,
    NetworkModule::class,
    OkHttpModule::class,
    FcmModule::class,
    NotificationModule::class,
    NavigationModule::class
])
interface AppComponent : AppProxyDependencies {
    fun inject(to: MessagingService)
}