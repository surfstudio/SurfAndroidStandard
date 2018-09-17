package ru.surfstudio.android.security.sample.interactor.profile

import io.reactivex.Observable
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.security.auth.SecureStorage
import javax.inject.Inject

@PerApplication
class ProfileInteractor @Inject constructor(private val secureStorage: SecureStorage) {

    fun getApiKey(pin: String): Observable<String> {
        return Observable.defer {
            Observable.just(secureStorage.getSecureData(pin) ?: throw SecurityException())
        }
    }

    fun signIn(apiKey: String, pin: String): Observable<Boolean> {
        return Observable.just(secureStorage.saveSecureData(apiKey, pin))
    }
}