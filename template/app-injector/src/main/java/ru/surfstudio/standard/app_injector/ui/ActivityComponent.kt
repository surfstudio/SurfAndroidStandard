package ru.surfstudio.standard.app_injector.ui

import android.content.Context
import android.content.SharedPreferences
import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.tabfragment.TabFragmentNavigator
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.notification.interactor.push.storage.FcmStorage
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.rxbus.RxBus
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import ru.surfstudio.standard.app_injector.AppComponent
import ru.surfstudio.standard.f_debug.memory.storage.MemoryDebugStorage
import ru.surfstudio.standard.f_debug.server_settings.reboot.interactor.RebootInteractor
import ru.surfstudio.standard.f_debug.server_settings.storage.DebugServerSettingsStorage
import ru.surfstudio.standard.i_initialization.InitializeAppInteractor
import javax.inject.Named

/**
 * Компонент для @PerActivity скоупа
 */
@PerActivity
@Component(dependencies = [AppComponent::class],
        modules = [ActivityModule::class])
interface ActivityComponent {
    fun schedulerProvider(): SchedulersProvider
    fun connectionProvider(): ConnectionProvider
    fun activityProvider(): ActivityProvider
    fun stringsProvider(): StringsProvider
    fun serverSettingsStorage(): DebugServerSettingsStorage
    fun rebootInteractor(): RebootInteractor
    fun memoryDebugStorage(): MemoryDebugStorage

    fun activityPersistentScope(): ActivityPersistentScope
    fun context(): Context
    fun fragmentNavigator(): FragmentNavigator
    fun tabFragmentNavigator(): TabFragmentNavigator
    fun globalNavigator(): GlobalNavigator
    fun rxBus(): RxBus
    fun fcmStorage(): FcmStorage
    @Named(NO_BACKUP_SHARED_PREF) fun sharedPreferences(): SharedPreferences

    fun initializeAppInteractor(): InitializeAppInteractor
}