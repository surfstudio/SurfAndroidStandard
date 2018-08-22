package ru.surfstudio.android.app.migration.sample.app.dagger

import android.content.Context
import dagger.Component
import ru.surfstudio.android.app.migration.sample.app.initialization.InitializeAppInteractor
import ru.surfstudio.android.app.migration.sample.app.initialization.migration.MigrationModule
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.sample.dagger.app.dagger.AppModule
import ru.surfstudio.android.sample.dagger.app.dagger.SharedPrefModule

@PerApplication
@Component(modules = [
    AppModule::class,
    SharedPrefModule::class,
    MigrationModule::class])
interface AppComponent {
    fun context(): Context
    fun activeActivityHolder(): ActiveActivityHolder
    fun connectionProvider(): ConnectionProvider
    fun schedulerProvider(): SchedulersProvider
    fun stringsProvider(): StringsProvider
    fun initializeAppInteractor(): InitializeAppInteractor
}
