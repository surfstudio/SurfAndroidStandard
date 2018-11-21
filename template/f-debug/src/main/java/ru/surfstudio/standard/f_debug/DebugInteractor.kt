package ru.surfstudio.standard.f_debug

import android.app.Application
import com.codemonkeylabs.fpslibrary.TinyDancer
import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.leakcanary.LeakCanary
import okhttp3.OkHttpClient
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.standard.f_debug.notification.DebugNotificationBuilder
import ru.surfstudio.standard.f_debug.server_settings.reboot.interactor.RebootInteractor
import ru.surfstudio.standard.f_debug.storage.DebugServerSettingsStorage
import ru.surfstudio.standard.f_debug.storage.DebugUiToolsStorage
import ru.surfstudio.standard.f_debug.storage.MemoryDebugStorage
import javax.inject.Inject

@PerApplication
class DebugInteractor @Inject constructor(
        private val memoryDebugStorage: MemoryDebugStorage,
        private val debugServerSettingsStorage: DebugServerSettingsStorage,
        private val debugUiToolsStorage: DebugUiToolsStorage,
        private val application: Application,
        private val rebootInteractor: RebootInteractor
) {

    //region настройки LeakCanary
    var isLeakCanaryEnabled: Boolean
        get() = memoryDebugStorage.isLeakCanaryEnabled
        set(value) {
            memoryDebugStorage.isLeakCanaryEnabled = value
        }

    var isFpsEnabled: Boolean
        get() = debugUiToolsStorage.isFpsEnabled
        set(value) {
            debugUiToolsStorage.isFpsEnabled = value
        }

    /**
     * Возвращает <pre>true</pre> если ненужно инициализировать [Application] иначе <pre>false</pre>
     * @return ненужно ли инициализировать [Application]
     */
    fun mustNotInitializeApp(): Boolean {
        return LeakCanary.isInAnalyzerProcess(application)
    }

    /**
     * Нужно вызвать в [Application.onCreate]
     */
    fun onCreateApp(icon: Int) {
        if (memoryDebugStorage.isLeakCanaryEnabled) {
            LeakCanary.install(application)
        }
        DebugNotificationBuilder.showDebugNotification(application, icon)
    }
    //endregion

    //region настройки запросов на сервер (Chuck)
    var isChuckEnabled: Boolean
        get() = debugServerSettingsStorage.isChuckEnabled
        set(value) {
            debugServerSettingsStorage.isChuckEnabled = value
        }

    /**
     * Добавляет [ChuckInterceptor] в [OkHttpClient] если в настройках включено
     */
    fun configureOkHttp(okHttpBuilder: OkHttpClient.Builder) {
        if (debugServerSettingsStorage.isChuckEnabled) {
            okHttpBuilder.addInterceptor(ChuckInterceptor(application))
        }

        if (debugUiToolsStorage.isFpsEnabled) {
            TinyDancer.create().show(application)
        }
    }
    //endregion

    fun reboot(route: ActivityRoute) {
        rebootInteractor.reboot(route)
    }
}