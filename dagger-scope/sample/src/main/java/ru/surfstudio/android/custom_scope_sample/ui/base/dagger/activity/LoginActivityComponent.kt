package ru.surfstudio.android.custom_scope_sample.ui.base.dagger.activity

import dagger.Component
import ru.surfstudio.android.custom_scope_sample.domain.EmailData
import ru.surfstudio.android.custom_scope_sample.ui.base.dagger.login.LoginComponent
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.rxbus.RxBus

/**
 * Компонент для @PerActivity скоупа
 * Но для конкретных активити, которые предназначены для логина.
 *
 * Наследование от ActivityComponent необходимо, дабы избежать дублирования.
 */
@PerActivity
@Component(dependencies = [(LoginComponent::class)],
        modules = [ActivityModule::class,
            LoginActivityModule::class])
interface LoginActivityComponent : ActivityComponent {
    fun rxBus(): RxBus
    fun loginData(): EmailData
}