package ru.surfstudio.android.security.sample.app.dagger

import android.content.Context
import dagger.Component
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultSharedPrefModule
import ru.surfstudio.android.security.auth.WrongEnterAttemptStorage
import ru.surfstudio.android.security.sample.interactor.profile.ProfileInteractor
import ru.surfstudio.android.security.session.SessionChangedInteractor
import ru.surfstudio.android.security.session.SessionManager

@PerApplication
@Component(modules = [
    CustomAppModule::class,
    DefaultSharedPrefModule::class
])
interface CustomAppComponent {
    fun context(): Context
    fun activeActivityHolder(): ActiveActivityHolder
    fun connectionProvider(): ConnectionProvider
    fun schedulerProvider(): SchedulersProvider
    fun stringsProvider(): StringsProvider

    fun sessionManager(): SessionManager
    fun sessionChangeInteractor(): SessionChangedInteractor
    fun wrongEnterPinAttemptStorage(): WrongEnterAttemptStorage
    fun profileInteractor(): ProfileInteractor
}
