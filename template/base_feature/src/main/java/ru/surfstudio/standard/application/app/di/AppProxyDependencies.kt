package ru.surfstudio.standard.application.app.di

import android.content.Context
import android.content.SharedPreferences
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.notification.PushHandler
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.standard.i_auth.AuthInteractor
import ru.surfstudio.standard.i_initialization.InitializeAppInteractor
import ru.surfstudio.standard.i_push_notification.storage.FcmStorage
import ru.surfstudio.standard.i_session.SessionChangedInteractor
import ru.surfstudio.standard.ui.mvi.navigation.IntentChecker
import javax.inject.Named

/**
 * Интерфейс, объединяющий в себе все зависимости в скоупе [PerApplication]
 * Следует использовать в компоненте Application и других компонентах более высоких уровней,
 * зависящих от него.
 */
interface AppProxyDependencies {
    fun initializeAppInteractor(): InitializeAppInteractor
    fun context(): Context
    fun activeActivityHolder(): ActiveActivityHolder
    fun connectionProvider(): ConnectionProvider
    fun sessionChangedInteractor(): SessionChangedInteractor
    fun schedulerProvider(): SchedulersProvider
    fun resourceProvider(): ResourceProvider
    fun globalNavigator(): GlobalNavigator
    fun intentChecker(): IntentChecker

    fun commandExecutor(): AppCommandExecutor
    fun activityNavigationProvider(): ActivityNavigationProvider
    fun screenResultObserver(): ScreenResultObserver

    fun fcmStorage(): FcmStorage
    fun pushHandler(): PushHandler

    @Named(NO_BACKUP_SHARED_PREF)
    fun sharedPreferences(): SharedPreferences

    fun authInteractor(): AuthInteractor
}