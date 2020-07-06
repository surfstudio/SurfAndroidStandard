package ru.surfstudio.standard.f_debug.injector.ui

import android.content.Context
import android.content.SharedPreferences
import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.tabfragment.TabFragmentNavigator
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import ru.surfstudio.standard.base.util.StringsProvider
import ru.surfstudio.standard.f_debug.DebugInteractor
import ru.surfstudio.standard.f_debug.injector.DebugAppComponent
import ru.surfstudio.standard.i_push.storage.FcmStorage
import javax.inject.Named

/**
 * Компонент для @PerActivity скоупа
 */
@PerActivity
@Component(dependencies = [DebugAppComponent::class],
        modules = [DebugActivityModule::class])
interface DebugActivityComponent {
    fun schedulerProvider(): SchedulersProvider
    fun connectionProvider(): ConnectionProvider
    fun activityProvider(): ActivityProvider
    fun stringsProvider(): StringsProvider
    fun debugInteractor(): DebugInteractor

    fun activityPersistentScope(): ActivityPersistentScope
    fun context(): Context
    fun fragmentNavigator(): FragmentNavigator
    fun tabFragmentNavigator(): TabFragmentNavigator
    fun globalNavigator(): GlobalNavigator
    fun fcmStorage(): FcmStorage
    @Named(NO_BACKUP_SHARED_PREF) fun sharedPreferences(): SharedPreferences
}