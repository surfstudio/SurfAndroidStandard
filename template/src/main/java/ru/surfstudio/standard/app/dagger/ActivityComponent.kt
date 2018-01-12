package ru.surfstudio.standard.app.dagger

import android.content.Context
import android.content.SharedPreferences

import dagger.Component
import ru.surfstudio.android.core.app.SharedPrefModule
import ru.surfstudio.android.core.app.bus.RxBus
import ru.surfstudio.android.core.app.dagger.scope.PerActivity
import ru.surfstudio.android.core.app.intialization.InitializationModule
import ru.surfstudio.android.core.app.intialization.migration.AppMigrationStorage
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider
import javax.inject.Named

@PerActivity
@Component(dependencies = [(AppComponent::class)])
interface ActivityComponent {
    fun context(): Context
    fun appMigrationStorage(): AppMigrationStorage
    fun schedulerProvider(): SchedulersProvider
    fun rxBus(): RxBus
    @Named(SharedPrefModule.NO_BACKUP_SHARED_PREF)
    fun noBackupSharedPreferences(): SharedPreferences

    @Named(InitializationModule.VERSION_CODE_PARAM)
    fun versionCode(): Int
}
