package ru.surfstudio.standard.ui.common.notification

import com.google.firebase.iid.FirebaseInstanceId
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.notification.ui.notification.FcmTokenProvider

/**
 * Модуль Firebase
 */
@Module
class FirebaseModule {
    @Provides
    @PerApplication
    fun fcmTokenProvider(): FcmTokenProvider = object : FcmTokenProvider {
        override fun provide(): String? = FirebaseInstanceId.getInstance().token
    }
}
