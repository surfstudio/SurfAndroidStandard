package ru.surfstudio.android.security.sample.interactor.profile

import io.reactivex.Single
import ru.surfstudio.android.dagger.scope.PerApplication
import javax.inject.Inject

private const val CORRECT_PIN = "0000"

@PerApplication
class ProfileInteractor @Inject constructor() {

    fun login(pin: String): Single<Boolean> = Single.just(pin == CORRECT_PIN)
}