package ru.surfstudio.android.security.sample.interactor.storage

import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.storage.BaseTextFileStorage
import ru.surfstudio.android.security.crypto.PinEncryptor
import javax.inject.Inject

@PerApplication
class ApiKeyStorageWrapper @Inject constructor(private val storage: BaseTextFileStorage) {

    companion object {
        private const val KEY_API_KEY_STORAGE = "api_key_storage"
    }

    fun saveApiKey(apiKey: String, pin: String) {
        storage.setEncryptor(PinEncryptor(pin))
        storage.put(KEY_API_KEY_STORAGE, apiKey)
    }

    fun getApiKey(pin: String): String? {
        storage.setEncryptor(PinEncryptor(pin))
        return storage.get(KEY_API_KEY_STORAGE)
    }

    fun clear() = storage.clear()
}