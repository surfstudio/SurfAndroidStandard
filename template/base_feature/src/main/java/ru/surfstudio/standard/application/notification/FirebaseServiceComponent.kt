package ru.surfstudio.standard.application.notification


import dagger.Component
import ru.surfstudio.android.notification.interactor.push.storage.FcmStorage
import ru.surfstudio.standard.application.app.di.AppComponent
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