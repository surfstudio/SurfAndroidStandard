package ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity

import android.content.Context
import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope
import ru.surfstudio.android.custom_scope_sample.domain.EmailData
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.login.LoginScreenComponent
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.rxbus.RxBus

/**
 * Компонент для @PerActivity скоупа
 */

@PerActivity
@Component(dependencies = [(LoginScreenComponent::class)],
        modules = [ActivityModule::class,
            SpecialActivityModule::class])
interface SpecialActivityComponent {
    fun schedulerProvider(): SchedulersProvider
    fun connectionProvider(): ConnectionProvider
    fun stringsProvider(): StringsProvider

    fun activityProvider(): ActivityProvider
    fun activityPersistentScope(): ActivityPersistentScope
    fun context(): Context
    fun rxBus(): RxBus

    fun loginData(): EmailData
}