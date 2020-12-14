package ru.surfstudio.android.push.sample.ui.common.notification

import dagger.Component
import ru.surfstudio.android.push.sample.app.dagger.CustomAppComponent
import javax.inject.Scope

@PerService
@Component(dependencies = [CustomAppComponent::class])
interface FirebaseServiceComponent {

    fun inject(s: FirebaseMessagingService)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerService