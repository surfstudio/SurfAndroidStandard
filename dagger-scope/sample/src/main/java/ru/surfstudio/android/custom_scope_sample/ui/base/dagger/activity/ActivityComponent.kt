package ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.ui.provider.Provider
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope
import ru.surfstudio.android.custom_scope_sample.app.dagger.AppComponent
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.sample.dagger.ui.base.StringsProvider

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

    fun activityProvider(): Provider<AppCompatActivity>
    fun activityPersistentScope(): ActivityPersistentScope
    fun context(): Context
}