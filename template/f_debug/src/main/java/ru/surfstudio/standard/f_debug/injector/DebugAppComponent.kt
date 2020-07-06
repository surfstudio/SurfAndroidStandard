package ru.surfstudio.standard.f_debug.injector

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Component
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import ru.surfstudio.standard.base.util.StringsProvider
import ru.surfstudio.standard.f_debug.DebugInteractor
import ru.surfstudio.standard.f_debug.injector.modules.DebugSharedPrefModule
import ru.surfstudio.standard.f_debug.injector.ui.notification.DebugFcmModule
import ru.surfstudio.standard.i_push.storage.FcmStorage
import javax.inject.Named

@PerApplication
@Component(modules = [
    DebugAppModule::class,
    DebugSharedPrefModule::class,
    DebugFcmModule::class
])
interface DebugAppComponent {
    fun context(): Context
    fun activeActivityHolder(): ActiveActivityHolder
    fun connectionProvider(): ConnectionProvider
    fun schedulerProvider(): SchedulersProvider
    fun stringsProvider(): StringsProvider
    fun globalNavigator(): GlobalNavigator
    fun application(): Application
    fun debugInteractor(): DebugInteractor
    fun fcmStorage(): FcmStorage
    @Named(NO_BACKUP_SHARED_PREF) fun sharedPreferences(): SharedPreferences
}