package ru.surfstudio.android.push.sample.app.dagger

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.notification.PushHandler
import ru.surfstudio.android.notification.interactor.push.PushInteractor
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultSharedPrefModule

@PerApplication
@Component(modules = [
    DefaultAppModule::class,
    DefaultSharedPrefModule::class,
    NotificationModule::class
])
interface CustomAppComponent : DefaultAppComponent {

    fun pushInteractor(): PushInteractor
    fun pushHandler(): PushHandler
}