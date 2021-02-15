package ru.surfstudio.android.custom_scope_sample.app.dagger

import android.content.Context
import dagger.Component
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider

@PerApplication
@Component(modules = [
    AppModule::class,
    SharedPrefModule::class])
interface AppComponent {
    fun context(): Context
    fun activeActivityHolder(): ActiveActivityHolder
    fun connectionProvider(): ConnectionProvider
    fun schedulerProvider(): SchedulersProvider
    fun resourceProvider(): ResourceProvider
}
