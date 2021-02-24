package ru.surfstudio.android.custom_scope_sample.ui.base.dagger.login

import android.content.Context
import dagger.Component
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.custom_scope_sample.app.dagger.AppComponent
import ru.surfstudio.android.custom_scope_sample.domain.EmailData
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.scope.PerLogin
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider

@PerLogin
@Component(dependencies = [AppComponent::class],
        modules = [LoginModule::class])
interface LoginComponent {
    fun context(): Context
    fun activeActivityHolder(): ActiveActivityHolder
    fun connectionProvider(): ConnectionProvider
    fun schedulerProvider(): SchedulersProvider
    fun resourceProvider(): ResourceProvider

    fun emailData(): EmailData
}