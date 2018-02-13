package ru.surfstudio.standard.app.dagger

import android.content.Context
import dagger.Component
import ru.surfstudio.android.core.app.bus.RxBus
import ru.surfstudio.android.core.app.connection.ConnectionProvider
import ru.surfstudio.android.core.app.dagger.scope.PerActivity
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider
import ru.surfstudio.standard.app.intialization.InitializeAppInteractor

@PerActivity
@Component(dependencies = [(AppComponent::class)])
interface ActivityComponent {
    fun context(): Context
    fun initializeAppInteractor(): InitializeAppInteractor
    fun schedulerProvider(): SchedulersProvider
    fun connectionProvider(): ConnectionProvider
    fun rxBus(): RxBus
    fun photoPresenter(): PhotoProvider
}
