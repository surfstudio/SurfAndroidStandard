package ru.surfstudio.android.sample.dagger.app.dagger

import android.content.Context
import android.content.SharedPreferences
import dagger.Component
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.provider.callbacks.ActivityNavigationProviderCallbacks
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import javax.inject.Named

@PerApplication
@Component(
    modules = [
        DefaultAppModule::class,
        NavigationModule::class,
        DefaultSharedPrefModule::class
    ]
)
interface DefaultAppComponent {
    fun context(): Context
    fun activeActivityHolder(): ActiveActivityHolder
    fun connectionProvider(): ConnectionProvider
    fun schedulerProvider(): SchedulersProvider
    fun resourceProvider(): ResourceProvider
    fun commandExecutor(): AppCommandExecutor
    fun permissionManager(): PermissionManager
    fun navigationProviderCallbacks(): ActivityNavigationProviderCallbacks

    @Named(NO_BACKUP_SHARED_PREF)
    fun sharedPreferences(): SharedPreferences
}
