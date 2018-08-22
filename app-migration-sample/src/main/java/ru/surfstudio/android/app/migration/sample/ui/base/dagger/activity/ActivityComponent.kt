package ru.surfstudio.android.app.migration.sample.ui.base.dagger.activity

import android.content.Context
import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.ui.bus.RxBus
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.app.migration.sample.app.dagger.AppComponent
import ru.surfstudio.android.app.migration.sample.app.initialization.InitializeAppInteractor
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.ActivityModule

/**
 * Компонент для @PerActivity скоупа
 */

@PerActivity
@Component(dependencies = [(AppComponent::class)],
        modules = [(ActivityModule::class)])
interface ActivityComponent {
    fun schedulerProvider(): SchedulersProvider
    fun connectionProvider(): ConnectionProvider
    fun stringsProvider(): StringsProvider
    fun initializeAppInteractor(): InitializeAppInteractor

    fun activityProvider(): ActivityProvider
    fun activityPersistentScope(): ActivityPersistentScope
    fun context(): Context
    fun rxBus(): RxBus
}