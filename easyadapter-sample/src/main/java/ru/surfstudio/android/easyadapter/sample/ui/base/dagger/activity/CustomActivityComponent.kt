package ru.surfstudio.android.easyadapter.sample.ui.base.dagger.activity

import android.content.Context
import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.ui.bus.RxBus
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.easyadapter.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.easyadapter.sample.interactor.FirstDataRepository
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule

/**
 * Компонент для @PerActivity скоупа
 */

@PerActivity
@Component(dependencies = [(CustomAppComponent::class)],
        modules = [(DefaultActivityModule::class)])
interface CustomActivityComponent {
    fun schedulerProvider(): SchedulersProvider
    fun connectionProvider(): ConnectionProvider
    fun stringsProvider(): StringsProvider

    fun activityProvider(): ActivityProvider
    fun activityPersistentScope(): ActivityPersistentScope
    fun context(): Context
    fun rxBus(): RxBus
    fun firstDataRepository(): FirstDataRepository
}