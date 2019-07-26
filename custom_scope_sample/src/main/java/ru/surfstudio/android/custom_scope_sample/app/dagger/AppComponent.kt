package ru.surfstudio.android.custom_scope_sample.app.dagger

import android.content.Context
import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.dagger.scope.PerApplication

@PerApplication
@Component(modules = [
    AppModule::class,
    SharedPrefModule::class])
interface AppComponent {
    fun context(): Context
    fun activeActivityHolder(): ActiveActivityHolder
    fun connectionProvider(): ConnectionProvider
    fun schedulerProvider(): SchedulersProvider
    fun stringsProvider(): StringsProvider
}
