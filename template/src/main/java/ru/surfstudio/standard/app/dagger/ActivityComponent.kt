package ru.surfstudio.standard.app.dagger

import android.content.Context

import dagger.Component
import ru.surfstudio.android.core.app.bus.RxBus
import ru.surfstudio.android.core.app.dagger.scope.PerActivity
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider

@PerActivity
@Component(dependencies = arrayOf(AppComponent::class))
interface ActivityComponent {
    fun context(): Context
    fun schedulerProvider(): SchedulersProvider
    fun rxBus(): RxBus
}
