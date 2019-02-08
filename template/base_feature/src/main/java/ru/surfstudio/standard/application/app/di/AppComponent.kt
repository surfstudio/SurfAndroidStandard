package ru.surfstudio.standard.application.app.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.standard.base.util.StringsProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.notification.PushHandler
import ru.surfstudio.standard.i_push_notification.storage.FcmStorage
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import ru.surfstudio.standard.application.auth.di.AuthModule
import ru.surfstudio.standard.application.cache.di.CacheModule
import ru.surfstudio.standard.i_initialization.InitializeAppInteractor
import ru.surfstudio.standard.i_session.SessionChangedInteractor
import ru.surfstudio.standard.application.migration.di.MigrationModule
import ru.surfstudio.standard.application.network.di.EtagModule
import ru.surfstudio.standard.application.network.di.NetworkModule
import ru.surfstudio.standard.application.network.di.OkHttpModule
import ru.surfstudio.standard.application.notification.FcmModule
import ru.surfstudio.standard.application.notification.MessagingService
import ru.surfstudio.standard.application.notification.NotificationModule
import ru.surfstudio.standard.application.storage.di.SharedPrefModule
import javax.inject.Named

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
    NotificationModule::class
])
interface AppComponent {
    fun initializeAppInteractor(): InitializeAppInteractor
    fun context(): Context
    fun activeActivityHolder(): ActiveActivityHolder
    fun connectionProvider(): ConnectionProvider
    fun sessionChangedInteractor(): SessionChangedInteractor
    fun schedulerProvider(): SchedulersProvider
    fun stringsProvider(): StringsProvider
    fun globalNavigator(): GlobalNavigator
    fun fcmStorage(): FcmStorage
    fun pushHandler(): PushHandler
    @Named(NO_BACKUP_SHARED_PREF)
    fun sharedPreferences(): SharedPreferences

    fun inject(to: MessagingService)
}