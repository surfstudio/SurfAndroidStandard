package ru.surfstudio.standard.app_injector.ui.notification

import dagger.Component
import ru.surfstudio.standard.app_injector.AppComponent
import javax.inject.Scope

@PerService
@Component(dependencies = [AppComponent::class])
interface FirebaseServiceComponent {

    fun inject(s: MessagingService)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerService