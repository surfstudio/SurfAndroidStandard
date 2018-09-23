package ru.surfstudio.android.security.sample.interactor.storage

import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.storage.BaseSerializableFileStorage
import ru.surfstudio.android.filestorage.storage.BaseTextFileStorage
import ru.surfstudio.android.security.crypto.PinEncryptor
import ru.surfstudio.android.security.sample.domain.ApiKey
import javax.inject.Inject

@PerApplication
class ApiKeyStorageWrapper
@Inject constructor(private val storage: BaseSerializableFileStorage<ApiKey>) {

    companion object {
        private const val KEY_API_KEY_STORAGE = "api_key_storage"
    }

    fun saveApiKey(apiKey: ApiKey, pin: String) {
        storage.setEncryptor(PinEncryptor(pin))
        storage.put(KEY_API_KEY_STORAGE, apiKey)
    }

    fun getApiKey(pin: String): ApiKey? {
        storage.setEncryptor(PinEncryptor(pin))
        return storage.get(KEY_API_KEY_STORAGE)
    }

    fun clear() = storage.clear()
}