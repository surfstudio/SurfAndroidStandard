package ru.surfstudio.standard.app.dagger

import android.content.Context

import dagger.Component
import ru.surfstudio.android.core.app.SharedPrefModule
import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.android.core.app.scheduler.SchedulerModule
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider
import ru.surfstudio.android.core.util.ActiveActivityHolder

@PerApplication
@Component(modules = arrayOf(AppModule::class, SchedulerModule::class, SharedPrefModule::class, ActiveActivityHolderModule::class))
interface AppComponent {
    fun context(): Context

    fun schedulerProvider(): SchedulersProvider

    fun activeActivityHolder(): ActiveActivityHolder

}
