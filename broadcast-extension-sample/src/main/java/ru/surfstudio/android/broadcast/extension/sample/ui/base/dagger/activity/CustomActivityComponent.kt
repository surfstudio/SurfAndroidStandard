package ru.surfstudio.android.broadcast.extension.sample.ui.base.dagger.activity

import dagger.Component
import ru.surfstudio.android.broadcast.extension.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.broadcast.extension.sample.interactor.SmsBroadcastReceiver
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule

/**
 * Компонент для @PerActivity скоупа
 */

@PerActivity
@Component(dependencies = [(CustomAppComponent::class)],
        modules = [(DefaultActivityModule::class)])
interface CustomActivityComponent : DefaultActivityComponent {
    fun smsBroadcastReceiver(): SmsBroadcastReceiver
}