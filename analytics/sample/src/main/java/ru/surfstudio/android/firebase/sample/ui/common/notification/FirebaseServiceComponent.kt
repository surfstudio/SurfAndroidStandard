package ru.surfstudio.android.firebase.sample.ui.common.notification

import dagger.Component
import ru.surfstudio.android.firebase.sample.app.dagger.CustomAppComponent
import javax.inject.Scope

@PerService
@Component(dependencies = [CustomAppComponent::class])
interface FirebaseServiceComponent {

    fun inject(s: FirebaseMessagingService)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerService