package ru.surfstudio.android.security.sample.interactor.profile

import io.reactivex.Observable
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.security.sample.domain.ApiKey
import ru.surfstudio.android.security.sample.interactor.storage.ApiKeyStorageWrapper
import javax.inject.Inject

@PerApplication
class ProfileInteractor
@Inject constructor(private val apiKeyStorageWrapper: ApiKeyStorageWrapper) {

    fun getApiKey(pin: String): Observable<ApiKey> {
        return Observable.defer {
            Observable.just(apiKeyStorageWrapper.getApiKey(pin) ?: throw SecurityException())
        }
    }

    fun signIn(apiKey: ApiKey, pin: String): Observable<Unit> {
        return Observable.defer {
            Observable.just(apiKeyStorageWrapper.saveApiKey(apiKey, pin))
        }
    }
}