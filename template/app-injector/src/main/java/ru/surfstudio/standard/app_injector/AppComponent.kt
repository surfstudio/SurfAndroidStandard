package ru.surfstudio.standard.app_injector

import android.content.ClipboardManager
import android.content.Context
import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.standard.app_injector.interactor.AuthModule
import ru.surfstudio.standard.app_injector.interactor.storage.SharedPrefModule
import ru.surfstudio.standard.app_injector.migration.MigrationModule
import ru.surfstudio.standard.app_injector.network.NetworkModule
import ru.surfstudio.standard.app_injector.network.OkHttpModule
import ru.surfstudio.standard.app_injector.ui.notification.MessagingService
import ru.surfstudio.standard.i_debug.DebugInteractor
import ru.surfstudio.standard.i_fcm.FcmStorage
import ru.surfstudio.standard.i_initialization.InitializeAppInteractor
import ru.surfstudio.standard.i_session.SessionChangedInteractor

@PerApplication
@Component(modules = [
    AppModule::class,
    MigrationModule::class,
    SharedPrefModule::class,
    AuthModule::class,
    NetworkModule::class,
    OkHttpModule::class])
interface AppComponent {
    fun initializeAppInteractor(): InitializeAppInteractor
    fun context(): Context
    fun activeActivityHolder(): ActiveActivityHolder
    fun connectionProvider(): ConnectionProvider
    fun sessionChangeInteractor(): SessionChangedInteractor
    fun schedulerProvider(): SchedulersProvider
    fun stringsProvider(): StringsProvider
    fun globalNavigator(): GlobalNavigator
    fun clipboardManager(): ClipboardManager

    fun fcmStorage(): FcmStorage
    fun debugInteractor(): DebugInteractor

    fun inject(to: MessagingService)
}