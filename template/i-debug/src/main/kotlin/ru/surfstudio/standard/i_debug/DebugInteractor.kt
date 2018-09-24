package ru.surfstudio.standard.i_debug

import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.i_fcm.FcmStorage
import javax.inject.Inject

@PerApplication
class DebugInteractor @Inject constructor(
        private val fcmStorage: FcmStorage) {

    fun saveFcmToken(fcmToken: String) {
        fcmStorage.fcmToken = fcmToken
    }

    fun getFcmToken(): String = fcmStorage.fcmToken

    fun clearFcmToken() = fcmStorage.clear()
}