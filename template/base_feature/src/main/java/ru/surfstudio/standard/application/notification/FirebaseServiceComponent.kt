package ru.surfstudio.standard.application.notification


import dagger.Component
import ru.surfstudio.standard.application.app.di.AppComponent
import ru.surfstudio.standard.i_push.storage.FcmStorage
import javax.inject.Scope

@PerService
@Component(dependencies = [AppComponent::class])
interface FirebaseServiceComponent {

    fun fcmStorage(): FcmStorage

    fun inject(s: MessagingService)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerService